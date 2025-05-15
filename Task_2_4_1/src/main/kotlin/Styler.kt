package org.example

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.IOException

class Styler(val checkstyleDir : String, val possibleCountWarnings : Int) {
    /**
     * Проверка код стайла у задач, ранее успешно скомпилированных. Используется checkstyle.jar
     * с конфигом config.xml.
     * Возвращает список успешно проверенных задач у соответствующих студентов.
     */
    fun checkStyleTasks(builtTasks : List<Pair<Tasks.Task, List<Pair<Groups.Group.Student, File>>>>)
            : List<Pair<Tasks.Task, List<Pair<Groups.Group.Student, File>>>> = runBlocking {
        val checkStyleJar = javaClass.getResource("/checkstyle.jar")
        val checkStyleConfig = javaClass.getResource("/config.xml")

        if (checkStyleJar == null) {
            System.err.println("error: checkstyle.jar not found")
            return@runBlocking emptyList()
        } else if (checkStyleConfig == null) {
            System.err.println("error: checkstyle.config not found")
            return@runBlocking emptyList()
        }

        val tempJar = File.createTempFile("checkstyle", ".jar")
        tempJar.outputStream().use { it.write(checkStyleJar.readBytes()) }
        tempJar.deleteOnExit()

        val jobs = builtTasks.map { pair ->
            async(Dispatchers.IO) {
                val res = checkStyleTask(pair.first.name, pair.second, tempJar, File(checkStyleConfig.toURI()))
                if (res.isEmpty())
                    null
                else Pair(pair.first, res)
            }
        }

        jobs.awaitAll().filterNotNull()
    }

    /**
     * Проверка код стайла в задаче у соответствующих студентов.
     */
    private fun checkStyleTask(name : String, students: List<Pair<Groups.Group.Student, File>>,
                               checkStyleJar : File, checkStyleConfig : File
    )
            : List<Pair<Groups.Group.Student, File>> {
        val runtime = Runtime.getRuntime()
        return students.mapNotNull { student ->
            try {
                val dir = File(student.second.absolutePath + File.separator + name)
                val args : List<String>
                val ret : Int
                val file = File(checkstyleDir).absolutePath + File.separator + student.first.nickname + "_" + name + "_out.txt"

                args = listOf("java", "-jar" , checkStyleJar.absolutePath, "-o",
                    file,
                    "-c", checkStyleConfig.toString(), "src")

                ret = ProcessBuilder()
                    .command(args)
                    .directory(dir)
                    .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                    .redirectError(ProcessBuilder.Redirect.DISCARD)
                    .start().waitFor()
                if (ret != 0) {
                    return@mapNotNull null
                }

                if (goodCheckStyle(file)) {
                    student
                } else null
            } catch (e : IOException) {
                System.err.println("error: ${e.message}")
                null
            }
        }
    }

    /**
     * Проверка критерия на успешное прохождение checkstyle.
     */
    private fun goodCheckStyle(fileName : String) : Boolean {
        return File(fileName).useLines { lines -> lines.count() } - 2 <= possibleCountWarnings
    }
}