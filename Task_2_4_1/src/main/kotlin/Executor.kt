package org.example

import java.io.File
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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

        val loader = Loader(workDir)
        val loadedRepos = loader.loadRepos(tasksForCheck)

        val builder = Builder();
        val builtTasks = builder.buildTasks(loadedRepos)
        reportBuilder.markBuilt(builtTasks)

        File(checkstyleDir).mkdir()

        val docer = Docer()
        val docTasks = docer.docTasks(builtTasks)
        reportBuilder.markDoc(docTasks);

        val styler = Styler(checkstyleDir, possibleCountWarnings)
        val styleTasks = styler.checkStyleTasks(builtTasks)
        reportBuilder.markStyle(styleTasks);

        val tester = Tester(tasks, groups, checkTasks)
        val res = tester.testTasks(builtTasks)
        reportBuilder.markTests(res)

        val file = File(workDir + File.separator + "results")
        file.mkdir()
        reportBuilder.build(File(workDir + File.separator + "results" + File.separator + "check_" +
                LocalDate.now().toString() + "_" +
                LocalTime.now().format(DateTimeFormatter.ofPattern("HH-mm")) + ".html"))


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
}