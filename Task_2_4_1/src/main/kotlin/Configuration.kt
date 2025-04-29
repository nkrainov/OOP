package org.example

class Tasks(var tasks: ArrayList<Task> = ArrayList()) {
    data class Task(var id: Int, var name: String,
               var max: Double, var soft: String,
               var hard: String)

    fun task(id: Int, name: String,
             max: Double, soft: String,
             hard: String){
        tasks.add(Task(id, name, max, soft, hard))
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

class CheckTasks(var tasks: List<CheckTask>) {
    class CheckTask(var task: List<Int>, var student: List<String>) {
        operator fun invoke(init: CheckTask.() -> Unit) {
            apply(init)
        }
    }

    operator fun invoke(init: CheckTasks.() -> Unit) {
        apply(init)
    }
}