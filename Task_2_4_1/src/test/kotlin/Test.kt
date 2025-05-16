package org.example

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.condition.DisabledIf
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.LocalDate

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CalculatorTest {
    companion object {
        var flagParse = true
        var flagLoader = true
        var flagBuilder = true
        var flagTester = true
        var flagDocer = true
        var flagStyler = true

        val script = """
        tasks {
            task("Task_2_2_1", 2.0, "07.03.2025", "14.03.2025")
            task("Task_2_3_1", 2.0, "28.03.2025", "04.04.2025")
        }

        groups {
            group("23216") {
                student("Nikita", "nkrainov", "https://github.com/nkrainov/OOP")
                student("Timofei", "ttomilov", "https://github.com/ttomilov/OOP")
            }

        }

        criteria {
            default("2")
            grade("5", 5)
            grade("4", 4)
            grade("3", 3)
        }

        checkTasks {
            task("Task_2_2_1") {
                checkFor("Nikita")
                checkFor("Timofei")
            }

            task("Task_2_3_1") {
                checkFor("Nikita")
                checkFor("Timofei")
            }
        }

        points {
            control("Control week", "21.12.2023")
        }
    """.trimIndent()
    }

    @TempDir
    lateinit var tempDir: Path

    private fun microservicesTestsFailed() : Boolean {
        return flagParse || flagDocer || flagStyler || flagBuilder || flagLoader || flagTester
    }

    private fun copyTestRepo() {
        val resource = javaClass.classLoader.getResource("testRepo")!!

        val path = Paths.get(resource.toURI())

        Files.walk(path).use { paths ->
            paths.forEach { source ->
                val relativePath = path.relativize(source)
                val destination = tempDir.resolve(relativePath)
                if (Files.isDirectory(source)) {
                    Files.createDirectories(destination)
                } else {
                    Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING)
                }
            }
        }
    }

    @Order(1)
    @Test
    fun testParse() {
        val file = File("check.checker")
        file.printWriter().use { out ->
            out.write(script)
        }

        val config = Config()
        config.readConfig()

        config.groups.groups.forEach { group ->
            assertEquals(group.name, "23216")
            group.students.forEach { student ->
                assertTrue(student.name in arrayOf("Nikita", "Timofei"))
            }
        }

        assertEquals(config.criteria.default, "2")
        config.criteria.grades.forEach { grade ->
            assertTrue(grade.first in arrayOf("2", "3", "4", "5"))
            assertEquals(grade.second.toString(), grade.first)
        }

        config.tasks.tasks.forEach { task ->
            assertTrue(task.name in arrayOf("Task_2_2_1", "Task_2_3_1"))
        }

        flagParse = false
    }

    @Order(1)
    @Test
    fun testLoader() {
        val workDir = File("checker")
        workDir.mkdirs()
        val loader = Loader("checker")
        val taskList = ArrayList<Pair<Tasks.Task, List<Groups.Group.Student>>>()

        taskList.add(Pair(Tasks.Task("Task_2_1_1", 1.0, LocalDate.now(), LocalDate.now()),
            listOf(Groups.Group.Student("Nikita", "nkrainov", "https://github.com/nkrainov/OOP"))))

        val ret = loader.loadRepos(taskList.toList())
        assertTrue(ret.size == 1)

        val fold = File("checker" + File.separator + "nkrainov")
        assertTrue(fold.exists())
        assertTrue(fold.isDirectory)
        fold.listFiles()?.let { assertFalse(it.isEmpty()) }

        workDir.deleteRecursively()

        flagLoader = false;
    }


    @Order(1)
    @Test
    fun testStyler() {
        copyTestRepo();

        File(tempDir.toString() + File.separator + "checkstyle").mkdir()
        val styler = Styler(tempDir.toString() + File.separator + "checkstyle", 10)
        val taskList = ArrayList<Pair<Tasks.Task, List<Pair<Groups.Group.Student, File>>>>()

        taskList.add(Pair(Tasks.Task("Task_2_1_1", 1.0, LocalDate.now(), LocalDate.now()),
            listOf(
                Pair(Groups.Group.Student("Nikita", "nkrainov", "https://github.com/nkrainov/OOP"),
                    tempDir.toFile()
                ))))

        val ret = styler.checkStyleTasks(taskList)
        assertTrue(File(tempDir.toString() + File.separator + "checkstyle").exists())
        File(tempDir.toString() + File.separator + "checkstyle").listFiles()?.let { assertFalse(it.isEmpty()) }


        flagStyler = false
    }

    @Order(1)
    @Test
    fun testTester() {
        copyTestRepo()

        val taskList = ArrayList<Pair<Tasks.Task, List<Pair<Groups.Group.Student, File>>>>()
        taskList.add(Pair(Tasks.Task("Task_2_1_1", 1.0, LocalDate.now(), LocalDate.now()),
            listOf(
                Pair(Groups.Group.Student("Nikita", "nkrainov", "https://github.com/nkrainov/OOP"),
                    tempDir.toFile()
                ))))

        val tester = Tester()

        val ret = tester.testTasks(taskList)

        assertTrue(ret.size == 1)
        assertTrue(ret[0].second[0].second.first == 3)
        assertTrue(ret[0].second[0].second.second == 0)
        assertTrue(ret[0].second[0].second.third == 0)

        flagTester = false
    }

    @Order(1)
    @Test
    fun testDocer() {
        copyTestRepo();

        val docer = Docer()
        val taskList = ArrayList<Pair<Tasks.Task, List<Pair<Groups.Group.Student, File>>>>()

        taskList.add(Pair(Tasks.Task("Task_2_1_1", 1.0, LocalDate.now(), LocalDate.now()),
            listOf(
                Pair(Groups.Group.Student("Nikita", "nkrainov", "https://github.com/nkrainov/OOP"),
                    tempDir.toFile()
                ))))

        val ret = docer.docTasks(taskList)
        assertTrue(ret.size == 1)
        assertTrue(File(tempDir.toString() + File.separator + "Task_2_1_1" + File.separator + "build"
                    + File.separator + "docs").exists())

        flagDocer = false
    }

    @Order(1)
    @Test
    fun testBuilder() {
        copyTestRepo();

        val builder = Builder()
        val taskList = ArrayList<Pair<Tasks.Task, List<Pair<Groups.Group.Student, File>>>>()

        taskList.add(Pair(Tasks.Task("Task_2_1_1", 1.0, LocalDate.now(), LocalDate.now()),
            listOf(
                Pair(Groups.Group.Student("Nikita", "nkrainov", "https://github.com/nkrainov/OOP"),
                    tempDir.toFile()
                ))))

        val ret = builder.buildTasks(taskList)
        assertTrue(ret.size == 1)
        assertTrue(File(tempDir.toString() + File.separator + "Task_2_1_1" + File.separator + "build").exists())


        flagBuilder = false
    }

    @Order(2)
    @Test
    @DisabledIf("microservicesTestsFailed")
    fun resultsMustExists() {
        println(7)
        val file = File("check.checker")
        file.printWriter().use { out ->
            out.write(script)
        }

        main(emptyArray<String>())

        assertTrue(File("checker").exists())
        assertTrue(File("checker" + File.separator + "results").exists())
        File("checker" + File.separator + "results" + File.separator).listFiles()?.let { assertFalse(it.isEmpty()) }
        assertTrue(File("checker" + File.separator + "checkstyle").exists())

        file.delete()
    }
}