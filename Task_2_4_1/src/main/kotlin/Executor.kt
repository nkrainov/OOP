package org.example

import java.io.File
import java.nio.file.Paths

//проверь ошибки ВЕЗДЕ
class Executor(private val tasks: Tasks, private val groups: Groups) {
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

    private fun find(needLoad : Boolean) : List<String> {
        val res = ArrayList<String>()
        val runtime = Runtime.getRuntime()
        for (group in groups.groups) {
            for (student in group.students) {
                val dir = student.name + group.name
                var args : Array<String>
                val folder = File(Paths.get("").toAbsolutePath().toString() + File.separator + dir)
                var ret : Int

                if (folder.exists()) {
                    if (!needLoad) {
                        res.add(dir)
                        continue
                    }

                    args = arrayOf("git", "pull")
                    ret = runtime.exec(args).waitFor()
                    if (ret != 0) {
                        System.err.println("Failed git pull")
                        continue
                    }

                    res.add(dir)
                    continue
                }

                if (!needLoad) continue

                args = arrayOf("git", "init", dir)
                ret = runtime.exec(args).waitFor()
                if (ret != 0) {
                    System.err.println("Failed git init")
                    continue
                }

                args = arrayOf("git", "remote", "add", "origin", student.link)

                ret = runtime.exec(args, null, folder).waitFor()
                if (ret != 0) {
                    System.err.println("Failed git remote add origin")
                    continue
                }

                args = arrayOf("git", "fetch")

                ret = runtime.exec(args, null, folder).waitFor()
                if (ret != 0) {
                    System.err.println("Failed git fetch")
                    continue
                }

                args = arrayOf("git", "checkout", "main")
                ret = Runtime.getRuntime().exec(args, null, File(dir)).waitFor()
                if (ret != 0) {
                    System.err.println("Failed git checkout main")
                    continue
                }

                res.add(dir)
            }
        }

        return res
    }

    private fun compile(list : List<String>) : List<String> {
        if (list.isEmpty()) return emptyList()

        val retList = ArrayList<String>()

        var args : Array<String>
        for (dir : String in list) {
            for (task : Tasks.Task in tasks.tasks) {
                val path = File(dir + File.separator + task.name + File.separator + "gradlew.bat").absolutePath.toString()

                args = arrayOf(path, "build")
                val ret : Int = Runtime.getRuntime().exec(args, null, File(dir + File.separator + task.name)).waitFor()
                if (ret != 0) {
                    System.err.println("Failed gradlew.bat build")
                    continue
                }

                retList.add(dir)
            }
        }

        return retList
    }

    private fun checkstyle(list: List<String>) : List<String> {
        val checkstyleJar = this.javaClass.getResource("checkstyle.jar")?.path
        for (dir in list) {

        }
        return list
    }

    private fun check() {
        doc(test(checkstyle(compile(find(true)))))
    }

    private fun checkstyle() {
        checkstyle(find(true))
    }

    private fun doc() {

    }

    private fun doc(list : List<String>) {

    }

    private fun test() {
        test(find(false))
    }

    private fun test(list: List<String>) : List<String> {
        for (dir : String in list) {
            for (task : Tasks.Task in tasks.tasks) {

                val path = File(dir + File.separator + task.name + File.separator + "gradlew.bat").absolutePath.toString()

                val args = arrayOf(path, "test")
                val ret : Int = Runtime.getRuntime().exec(args, null, File(dir + File.separator + task.name)).waitFor()
                if (ret != 0) {
                    println("error2")
                    continue
                }
                println("good test!")
            }
        }

        return emptyList() //TEMPORARY
    }

    private fun calc() {

    }

    private fun report() {

    }

    private fun compile() {
        compile(find(false))
    }
}