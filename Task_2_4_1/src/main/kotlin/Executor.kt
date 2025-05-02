package org.example

import java.io.File
import kotlinx.coroutines.*
import java.io.IOException
import javax.xml.parsers.DocumentBuilderFactory
import java.time.LocalDate

class Executor(private val tasks: Tasks, val groups: Groups,
               private val checkTasks: CheckTasks, val criteria: Criteria, val points: ControlPoints) {

    constructor(config : Config) : this(config.tasks, config.groups, config.checkTasks, config.criteria, config.points)

    private val workDir = "checker"
    private val checkstyleDir = workDir + File.separator + "checkstyle"

    private val possibleCountWarnings = 10

    fun execute() {
        val tasksForCheck = getTasksForCheck()
        if (tasksForCheck.isEmpty()) {
            throw Exception("No tasks found")
        }

        File(workDir).mkdir()

        val reportBuilder = ReportBuilder(tasksForCheck)

        val loadedRepos = loadRepos(tasksForCheck)

        val builtTasks = buildTasks(loadedRepos)
        reportBuilder.markBuilt(builtTasks)

        File(checkstyleDir).mkdir()

        val docTasks = docTasks(builtTasks)
        reportBuilder.markDoc(docTasks);

        val styleTasks = checkStyleTasks(builtTasks)
        reportBuilder.markStyle(styleTasks);

        val res = testTasks(builtTasks)
        reportBuilder.markTests(res)

        val file = File(workDir + File.separator + "results")
        file.mkdir()
        reportBuilder.build(File(workDir + File.separator + "results" + File.separator + "check" + LocalDate.now().toString() + ".html"))


    }


    /**
     * Получаем пары Task и соответствующих им List<Student>
    */
    private fun getTasksForCheck() : List<Pair<Tasks.Task, List<Groups.Group.Student>>> {
        val checkTaskMap: Map<String, CheckTasks.CheckTask> = checkTasks.tasks.associateBy { it.task }
        val studentsMap = groups.groups.flatMap { it -> it.students }.associateBy { it.name }

        val ret = tasks.tasks.mapNotNull { task ->
            val checkTask = checkTaskMap[task.name]
            val students = checkTask?.students?.mapNotNull {studentsMap[it]}
            if (students != null) Pair(task, students) else null
        }

        return ret
    }

    /**
     * Загружаем (или обновляем) репозитории для каждого студента, у которых мы проверим задачи.
     * На каждую загрузку выделяется своя корутина. Возвращает список пар задач и студентов, чьи
     * репозитории были успешно загружены.
     */
    private fun loadRepos(tasksForCheck: List<Pair<Tasks.Task, List<Groups.Group.Student>>>) :
            List<Pair<Tasks.Task, List<Pair<Groups.Group.Student, File>>>> = runBlocking {

        val jobs = tasksForCheck.flatMap {it.second}.distinct().map { student ->
            val normalizedRepo = student.link.trim()
            async(Dispatchers.IO) {
                val file = loadRepo(normalizedRepo, student.nickname)
                if (file != null) Pair(student, file) else null
            }
        }

        val res = jobs.awaitAll().filterNotNull().distinct().associateBy { it.first }

        tasksForCheck.mapNotNull {pair ->
            val newList = pair.second.mapNotNull { elem ->
                val file = res[elem]?.second
                if (file != null) Pair(elem, file) else null
            }
            if (newList.isEmpty()) null else Pair(pair.first, newList)
        }
    }

    /**
     * Загрузка отдельного репозитория. Создаем папку в соответствии с nickname,
     * затем делаем git init, git fetch, git checkout. Если репозиторий уже загружен,
     * то делаем git pull. Возвращает File, указывающий на директорию загруженного репозитория.
     */
    private fun loadRepo(normalizedRepo: String, nickname : String) : File? {
        var flag = false
        val dir = File(workDir + File.separator + nickname)
        try {
            var args : Array<String>
            val runtime = Runtime.getRuntime()
            var ret : Int

            if (dir.exists()) {
                args = arrayOf("git", "pull")
                ret = runtime.exec(args, null, dir).waitFor()
                if (ret != 0) {
                    return null
                }
            } else {
                if (!dir.mkdir()) return null

                args = arrayOf("git", "init", nickname)
                ret = runtime.exec(args, null, File(workDir)).waitFor()
                if (ret != 0) {
                    return null
                }

                args = arrayOf("git", "remote", "add", "origin", normalizedRepo)
                ret = runtime.exec(args, null, dir).waitFor()
                if (ret != 0) {
                    return null
                }

                args = arrayOf("git", "fetch")
                ret = runtime.exec(args, null, dir).waitFor()
                if (ret != 0) {
                    return null
                }

                args = arrayOf("git", "checkout", "main")
                ret = runtime.exec(args, null, dir).waitFor()
                if (ret != 0) {
                    return null
                }
            }

            flag = true
            return dir
        } catch (e : IOException) {
            System.err.println("error: ${e.message}")
            return null
        } finally {
            if (!flag) {
                dir.deleteRecursively()
            }
        }
    }

    /**
     * Компиляция и тестирование задач для соответствующих студентов.
     * Возвращает список пар задач и студентов, для кого это успешно было сделано.
     */
    private fun buildTasks(loadedRepos: List<Pair<Tasks.Task, List<Pair<Groups.Group.Student, File>>>>)
    : List<Pair<Tasks.Task, List<Pair<Groups.Group.Student, File>>>> = runBlocking {
        val jobs = loadedRepos.map { pair ->
            async(Dispatchers.IO) {
                val res = buildTask(pair.first.name, pair.second)
                if (res.isEmpty()) null else Pair(pair.first, res)
            }
        }

        jobs.awaitAll().filterNotNull()
    }

    /**
     * Компиляция задачи у соответствующих студентов. Зовется gradlew (для Windows - gradlew.bat).
     * Возвращает список студентов, с которыми это получилось.
     */
    private fun buildTask(name: String, students: List<Pair<Groups.Group.Student, File>>)
        : List<Pair<Groups.Group.Student, File>> {
        val runtime = Runtime.getRuntime()
        return students.mapNotNull { student ->
            try {
                val dir = File(student.second.absolutePath + File.separator + name)
                val args : Array<String>
                val ret : Int

                if (dir.exists()) {
                    args = if (System.getProperty("os.name").lowercase().contains("win")) {
                        arrayOf("./gradlew.bat", "assemble")
                    } else {
                        arrayOf("./gradlew", "assemble")
                    }

                    ret = runtime.exec(args, null, dir).waitFor()
                    if (ret != 0) {
                        return@mapNotNull null
                    }

                    student
                } else null
            } catch (e : IOException) {
                System.err.println("error: ${e.message}")
                null
            }
        }
    }

    private fun docTasks(builtTasks: List<Pair<Tasks.Task, List<Pair<Groups.Group.Student, File>>>>) : List<Pair<Tasks.Task, List<Pair<Groups.Group.Student, File>>>> = runBlocking {
        val jobs = builtTasks.map { pair ->
            async(Dispatchers.IO) {
                val res = docTask(pair.first.name, pair.second)
                if (res.isEmpty()) null else Pair(pair.first, res)
            }
        }

        jobs.awaitAll().filterNotNull()
    }

    private fun docTask(name: String, students: List<Pair<Groups.Group.Student, File>>)
        : List<Pair<Groups.Group.Student, File>> {
        val runtime = Runtime.getRuntime()
        return students.mapNotNull { student ->
            try {
                val dir = File(student.second.absolutePath + File.separator + name)
                val args : List<String>
                val ret : Int

                if (dir.exists()) {
                    args = if (System.getProperty("os.name").lowercase().contains("win")) {
                        listOf("./gradlew.bat", "javadoc")
                    } else {
                        listOf("./gradlew", "javadoc")
                    }

                    ret = ProcessBuilder()
                        .command(args)
                        .directory(dir)
                        .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                        .redirectError(ProcessBuilder.Redirect.DISCARD)
                        .start().waitFor()
                    if (ret != 0) {
                        return@mapNotNull null
                    }

                    student
                } else null
            } catch (e : IOException) {
                System.err.println("error: ${e.message}")
                null
            }
        }
    }

    /**
     * Проверка код стайла у задач, ранее успешно скомпилированных. Используется checkstyle.jar
     * с конфигом config.xml.
     * Возвращает список успешно проверенных задач у соответствующих студентов.
     */
    private fun checkStyleTasks(builtTasks : List<Pair<Tasks.Task, List<Pair<Groups.Group.Student, File>>>>)
    : List<Pair<Tasks.Task, List<Pair<Groups.Group.Student, File>>>> = runBlocking {
        val checkStyleJar = javaClass.getResource("/checkstyle.jar")
        val checkStyleConfig = javaClass.getResource("/config.xml")

        if (checkStyleJar == null) {
            System.err.println("error: checkstyle.jar not found")
            return@runBlocking emptyList()
        } else if (checkStyleConfig == null) {
            System.err.println("error: checkstyle.config not found")
            return@runBlocking emptyList()
        }

        val tempJar = File.createTempFile("checkstyle", ".jar")
        tempJar.outputStream().use { it.write(checkStyleJar.readBytes()) }
        tempJar.deleteOnExit()

        val jobs = builtTasks.map { pair ->
            async(Dispatchers.IO) {
                val res = checkStyleTask(pair.first.name, pair.second, tempJar, File(checkStyleConfig.toURI()))
                if (res.isEmpty()) null else Pair(pair.first, res)
            }
        }

        jobs.awaitAll().filterNotNull()
    }

    /**
     * Проверка код стайла в задаче у соответствующих студентов.
     */
    private fun checkStyleTask(name : String, students: List<Pair<Groups.Group.Student, File>>,
            checkStyleJar : File, checkStyleConfig : File)
        : List<Pair<Groups.Group.Student, File>> {
        val runtime = Runtime.getRuntime()
        return students.mapNotNull { student ->
            try {
                val dir = File(student.second.absolutePath + File.separator + name)
                val args : Array<String>
                val ret : Int
                val file = File(checkstyleDir).absolutePath + File.separator + student.first.nickname + "_" + name + "_out.txt"

                args = arrayOf("java", "-jar" , checkStyleJar.absolutePath, "-o",
                    file,
                    "-c", checkStyleConfig.toString(), "src")

                ret = runtime.exec(args, null, dir).waitFor()
                if (ret != 0) {
                    return@mapNotNull null
                }

                if (goodCheckStyle(file)) {
                    student
                } else null
            } catch (e : IOException) {
                System.err.println("error: ${e.message}")
                null
            }
        }
    }

    /**
     * Проверка критерия на успешное прохождение checkstyle.
     */
    private fun goodCheckStyle(fileName : String) : Boolean {
        return File(fileName).useLines { lines -> lines.count() } - 2 <= possibleCountWarnings
    }

    private fun testTasks(builtTasks: List<Pair<Tasks.Task, List<Pair<Groups.Group.Student, File>>>>)
            : List<Pair<Tasks.Task, List<Pair<Groups.Group.Student, Triple<Int, Int, Int>>>>> = runBlocking {
        val jobs = builtTasks.map { pair ->
            async(Dispatchers.IO) {
                val res = testTask(pair.first.name, pair.second)
                if (res.isEmpty()) null else Pair(pair.first, res)
            }
        }

        jobs.awaitAll().filterNotNull()
    }


    private fun testTask(name : String, students: List<Pair<Groups.Group.Student, File>>) : List<Pair<Groups.Group.Student, Triple<Int, Int, Int>>> {
        val runtime = Runtime.getRuntime()
        return students.mapNotNull { student ->
            try {
                val dir = File(student.second.absolutePath + File.separator + name)
                val args : List<String>
                val ret : Int

                args = if (System.getProperty("os.name").lowercase().contains("win")) {
                    listOf("./gradlew.bat", "test")
                } else {
                    listOf("./gradlew", "test")
                }

                ret = ProcessBuilder()
                    .command(args)
                    .directory(dir)
                    .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                    .redirectError(ProcessBuilder.Redirect.DISCARD)
                    .start().waitFor()

                val resDir = File(student.second.absolutePath + File.separator + name + File.separator
                        + "build" + File.separator + "test-results" + File.separator + "test")
                var success = 0
                var skipped = 0
                var failure = 0
                for (file in resDir.listFiles()!!) {
                    if (file.toString().contains("TEST-")) {
                        val res = parseTestResults(file)
                        success += res.first
                        failure += res.second
                        skipped += res.third
                    }
                }
                Pair(student.first, Triple(success, failure, skipped))
            } catch (e : IOException) {
                System.err.println("error: ${e.message}")
                null
            } catch (e : NullPointerException) {
                System.err.println("test results for ${student.first.name}")
                null
            }
        }
    }

    private fun parseTestResults(xmlContent: File): Triple<Int, Int, Int> {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val document = builder.parse(xmlContent)
        val testSuite = document.documentElement

        val total = testSuite.getAttribute("tests").toInt()
        val failures = testSuite.getAttribute("failures").toInt()
        val errors = testSuite.getAttribute("errors").toInt()
        val skipped = testSuite.getAttribute("skipped").toInt()

        val failedTests = failures + errors
        val successfulTests = total - failedTests

        return Triple(successfulTests, failedTests, skipped)
    }
}