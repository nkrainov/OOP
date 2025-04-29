package org.example

enum class Command{
    check, load, compile, checkstyle, doc, test, calc, report, noCommand
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        printHelp();
        return
    }

    val command = getCommand(args[0])
    if (command == Command.noCommand) {
        printHelp()
        return
    }

    val config = Config()
    config.readConfig()

    val executor = Executor(config.tasks, config.groups)
    executor.execute(command)
}

private fun printHelp() {
    println("""
        usage: checker [command]
        
        load            load repos
        compile         compile task from repositories
        checkstyle      check code style for tasks 
        test            test tasks
    """.trimIndent())
}

private fun getCommand(name: String): Command {
    return when (name) {
        "check" -> Command.check
        "compile" -> Command.compile
        "checkstyle" -> Command.checkstyle
        "test" -> Command.test
        "report" -> Command.report
        "load" -> Command.load
        "doc" -> Command.doc
        "calc" -> Command.calc
        else -> Command.noCommand
    }
}