package org.example

import kotlinx.coroutines.*
import java.io.File
import java.io.IOException

class Builder {
    /**
     * Компиляция задач для соответствующих студентов.
     * Возвращает список пар задач и студентов, для кого это успешно было сделано.
     */
    fun buildTasks(loadedRepos: List<Pair<Tasks.Task, List<Pair<Groups.Group.Student, File>>>>)
            : List<Pair<Tasks.Task, List<Pair<Groups.Group.Student, File>>>> = runBlocking {
        val jobs = loadedRepos.map { pair ->
            async(Dispatchers.IO) {
                val res = buildTask(pair.first.name, pair.second)
                if (res.isEmpty()) null else Pair(pair.first, res)
            }
        }

        jobs.awaitAll().filterNotNull()
    }

    /**
     * Компиляция задачи у соответствующих студентов. Зовется gradlew (для Windows - gradlew.bat).
     * Возвращает список студентов, с которыми это получилось.
     */
    private fun buildTask(name: String, students: List<Pair<Groups.Group.Student, File>>)
            : List<Pair<Groups.Group.Student, File>> {
        val runtime = Runtime.getRuntime()
        return students.mapNotNull { student ->
            try {
                val dir = File(student.second.absolutePath + File.separator + name)
                val args : Array<String>
                val ret : Int

                if (dir.exists()) {
                    args = if (System.getProperty("os.name").lowercase().contains("win")) {
                        arrayOf("./gradlew.bat", "assemble")
                    } else {
                        arrayOf("./gradlew", "assemble")
                    }

                    ret = runtime.exec(args, null, dir).waitFor()
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