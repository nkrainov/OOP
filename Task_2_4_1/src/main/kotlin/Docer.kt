package org.example

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.IOException

class Docer {
    public fun docTasks(builtTasks: List<Pair<Tasks.Task, List<Pair<Groups.Group.Student, File>>>>) : List<Pair<Tasks.Task, List<Pair<Groups.Group.Student, File>>>> = runBlocking {
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
                        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                        .redirectError(ProcessBuilder.Redirect.INHERIT)
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
}