package org.example

import java.io.File
import java.nio.file.Paths

class Executor(val tasks: Tasks, val groups: Groups) {
    fun execute(command: Command) {
        when (command) {
            Command.doc -> doc()
            Command.calc -> calc()
            Command.load -> find(true)
            Command.test -> test()
            Command.report -> report()
            Command.compile -> compile()
            Command.check -> check()
            Command.checkstyle -> checkstyle()
            Command.noCommand -> return
        }
    }

    private fun find(needLoad : Boolean) : List<Groups.Group.Student> {
        val res = ArrayList<Groups.Group.Student>()
        val runtime = Runtime.getRuntime()
        for (group in groups.groups) {
            for (student in group.students) {
                val dir = student.name + group.name
                var args : Array<String>;
                val folder = File(Paths.get("").toAbsolutePath().toString() + File.separator + dir)
                var ret : Int

                if (folder.exists()) {
                    if (!needLoad) {
                        res.add(student)
                        continue
                    }

                    args = arrayOf("git", "fetch")
                    ret = runtime.exec(args).waitFor()
                    if (ret != 0) {
                        println("error")
                        continue
                    }

                    res.add(student)
                    continue
                }

                if (!needLoad) continue

                args = arrayOf("git", "init", dir)
                ret = runtime.exec(args).waitFor()
                if (ret != 0) {
                    println("error")
                    continue
                }

                args = arrayOf("git", "remote", "add", "origin", student.link)

                ret = runtime.exec(args, null, folder).waitFor()
                if (ret != 0) {
                    println("error")
                    continue
                }

                res.add(student)
            }
        }

        return res
    }

    private fun check() {
        find(true)
        compile()
        test()
        checkstyle()
    }

    fun checkstyle() {

    }

    fun doc() {

    }

    fun test() {

    }

    fun calc() {

    }

    fun report() {

    }

    fun compile() {
        
    }
}