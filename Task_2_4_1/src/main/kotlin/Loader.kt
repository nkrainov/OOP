package org.example

import kotlinx.coroutines.*
import java.io.File
import java.io.IOException

class Loader(val workDir: String) {

    /**
     * Загружаем (или обновляем) репозитории для каждого студента, у которых мы проверим задачи.
     * На каждую загрузку выделяется своя корутина. Возвращает список пар задач и студентов, чьи
     * репозитории были успешно загружены.
     */
    fun loadRepos(tasksForCheck: List<Pair<Tasks.Task, List<Groups.Group.Student>>>) :
            List<Pair<Tasks.Task, List<Pair<Groups.Group.Student, File>>>> = runBlocking {

        val jobs = tasksForCheck.flatMap {it.second}.distinct().map { student ->
            val normalizedRepo = student.link.trim()
            async(Dispatchers.IO) {
                val file = loadRepo(normalizedRepo, student.nickname)
                if (file != null) Pair(student, file) else null
            }
        }

        val res = jobs.awaitAll().filterNotNull().distinct().associateBy { it.first }

        tasksForCheck.mapNotNull {pair ->
            val newList = pair.second.mapNotNull { elem ->
                val file = res[elem]?.second
                if (file != null) Pair(elem, file) else null
            }
            if (newList.isEmpty()) null else Pair(pair.first, newList)
        }
    }

    /**
     * Загрузка отдельного репозитория. Создаем папку в соответствии с nickname,
     * затем делаем git init, git fetch, git checkout. Если репозиторий уже загружен,
     * то делаем git pull. Возвращает File, указывающий на директорию загруженного репозитория.
     */
    private fun loadRepo(normalizedRepo: String, nickname : String) : File? {
        var flag = false
        val dir = File(workDir + File.separator + nickname)
        try {
            var args : Array<String>
            val runtime = Runtime.getRuntime()
            var ret : Int

            if (dir.exists()) {
                args = arrayOf("git", "pull")
                ret = runtime.exec(args, null, dir).waitFor()
                if (ret != 0) {
                    return null
                }
            } else {
                if (!dir.mkdir()) return null

                args = arrayOf("git", "init", nickname)
                ret = runtime.exec(args, null, File(workDir)).waitFor()
                if (ret != 0) {
                    return null
                }

                args = arrayOf("git", "remote", "add", "origin", normalizedRepo)
                ret = runtime.exec(args, null, dir).waitFor()
                if (ret != 0) {
                    return null
                }

                args = arrayOf("git", "fetch")
                ret = runtime.exec(args, null, dir).waitFor()
                if (ret != 0) {
                    return null
                }

                args = arrayOf("git", "checkout", "main")
                ret = runtime.exec(args, null, dir).waitFor()
                if (ret != 0) {
                    return null
                }
            }

            flag = true
            return dir
        } catch (e : IOException) {
            System.err.println("error: ${e.message}")
            return null
        } finally {
            if (!flag) {
                dir.deleteRecursively()
            }
        }
    }
}