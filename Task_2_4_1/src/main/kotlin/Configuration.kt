package org.example

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Tasks(var tasks: ArrayList<Task> = ArrayList()) {
    data class Task(var name: String,
               var max: Double, var soft: LocalDate,
               var hard: LocalDate)

    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    fun task(name: String,
             max: Double, soft: String,
             hard: String){
        val softDate = LocalDate.parse(soft, formatter)
        val hardDate = LocalDate.parse(hard, formatter)
        tasks.add(Task(name, max, softDate, hardDate))
    }


    operator fun invoke(init: Tasks.() -> Unit) {
        apply(init)
    }
}

class Groups(var groups: ArrayList<Group> = ArrayList()) {
     class Group(var name: String,
                 var students: ArrayList<Student>) {

         data class Student(var name: String = "",
               var nickname: String = "",
               var link: String = "")

         fun student(name: String = "",
                     nickname: String = "",
                     link: String = "")  {
             students.add(Student(name, nickname, link))

         }
    }

    fun group(name: String, init: Group.() -> Unit) : Group {
        val group = Group(name, ArrayList())
        group.name = name
        groups.add(group)
        init(group)
        return group
    }


    operator fun invoke(init: Groups.() -> Unit) {
        apply(init)
    }
}

class CheckTasks(var tasks: ArrayList<CheckTask> = ArrayList()) {
    class CheckTask(var task: String, var students: ArrayList<String>) {
        fun checkFor(name : String) {
            students.add(name)
        }
    }

    fun task(task: String, init: CheckTask.() -> Unit) {
        val checkTask = CheckTask(task, ArrayList())
        tasks.add(checkTask.apply(init))
    }

    operator fun invoke(init: CheckTasks.() -> Unit) {
        apply(init)
    }

    operator fun iterator(): Iterator<CheckTask> = tasks.iterator()
}

class ControlPoints(var points: ArrayList<ControlPoint> = ArrayList()) {
    data class ControlPoint(val name : String, val date : LocalDate)

    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    fun control(name : String, date: String) {
        points.add(ControlPoint(name, LocalDate.parse(date, formatter)))
    }

    operator fun invoke(init: ControlPoints.() -> Unit) {
        apply(init)
    }
}

class Criteria(var grades : ArrayList<Pair<String, Int>> = ArrayList(), var default : String = "0") {

    fun default(name: String) {
        default = name
    }

    fun grade(name : String, value: Int) {
        grades.add(Pair(name, value))
    }

    operator fun invoke(init: Criteria.() -> Unit) {
        apply(init)
        grades.sortByDescending { it.second }
    }
}