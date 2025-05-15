package org.example

import java.io.File
import kotlinx.html.*
import kotlinx.html.stream.appendHTML

class ReportBuilder() {
    data class TaskEntry(var build : Boolean,
                         var doc : Boolean,
                         var style : Boolean,
                         var success : Int,
                         var failure : Int,
                         var skipped : Int,
                         var sum: Int)

    private data class GeneralTable(private val students : Map<String, TaskEntry>)

    private data class TaskTable(val students : Map<String, TaskEntry>)

    private val tasksTables : HashMap<String, TaskTable> = HashMap()

    constructor(list : List<Pair<Tasks.Task, List<Groups.Group.Student>>>) : this() {
        for (pair in list) {
            val hashMap = HashMap<String, TaskEntry>()
            pair.second.forEach {
                hashMap[it.name] = TaskEntry(false, false, false,0, 0, 0, 0)
            }
            val table = TaskTable(hashMap)
            tasksTables[pair.first.name] = table
        }
    }

    fun markBuilt(list : List<Pair<Tasks.Task, List<Pair<Groups.Group.Student, File>>>>) {
        for (pair in list) {
            pair.second.forEach {
                val res = tasksTables[pair.first.name]?.students?.get(it.first.name)
                if (res != null) res.build = true
            }
        }
    }

    fun markDoc(docTasks: List<Pair<Tasks.Task, List<Pair<Groups.Group.Student, File>>>>) {
        for (pair in docTasks) {
            pair.second.forEach {
                val res = tasksTables[pair.first.name]?.students?.get(it.first.name)
                if (res != null) res.doc = true
            }
        }
    }

    fun markStyle(styleTasks: List<Pair<Tasks.Task, List<Pair<Groups.Group.Student, File>>>>) {
        for (pair in styleTasks) {
            pair.second.forEach {
                val res = tasksTables[pair.first.name]?.students?.get(it.first.name)
                if (res != null) res.style = true
            }
        }
    }

    fun markTests(list: List<Pair<Tasks.Task, List<Pair<Groups.Group.Student, Triple<Int, Int, Int>>>>>) {
        for (pair in list) {
            pair.second.forEach {
                val res = tasksTables[pair.first.name]?.students?.get(it.first.name)
                if (res != null) {
                    res.success = it.second.first
                    res.failure = it.second.second
                    res.skipped = it.second.third
                }
            }
        }
    }

    fun build(file: File) {
        val writer = file.writer()



        val html = buildString {
            appendHTML().html {
                body {
                    tasksTables.forEach { (t, u) ->
                        a { + t}
                        table {
                            tr {
                                th { +"student"}
                                th { +"build"}
                                th { +"doc"}
                                th { +"style"}
                                th { +"tests(success,failure,skipped)"}
                                th { +"score"}
                            }
                            u.students.forEach { (student, entry) ->
                                tr {
                                    td { +student}
                                    td { +entry.build.toString()}
                                    td { +entry.doc.toString()}
                                    td { +entry.style.toString()}
                                    td { + (entry.success.toString() + "/" + entry.failure.toString() + "/" + entry.skipped.toString()) }
                                    td { +calcSum(entry).toString()}
                                }
                            }
                        }

                    }
                }
            }
        }

        writer.use {
            it.write(html)
        }
    }

    private fun calcSum(entry : TaskEntry) : Int {
         return if (entry.success > 0 && entry.failure == 0 && entry.skipped == 0 && entry.style && entry.doc && entry.build) 1 else 0
    }
}