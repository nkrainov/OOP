package org.example

fun main(args: Array<String>) {
    val config = Config()
    try {
        config.readConfig()
    } catch (e: Exception) {
        System.err.println("error: ${e.message}")
        return
    }

    Executor(config).execute()
}