package org.example

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.IOException
import javax.xml.parsers.DocumentBuilderFactory

class Tester() {
    fun testTasks(builtTasks: List<Pair<Tasks.Task, List<Pair<Groups.Group.Student, File>>>>)
            : List<Pair<Tasks.Task, List<Pair<Groups.Group.Student, Triple<Int, Int, Int>>>>> = runBlocking {
        val jobs = builtTasks.map { pair ->
            async(Dispatchers.IO) {
                val res = testTask(pair.first.name, pair.second)
                if (res.isEmpty()) null else Pair(pair.first, res)
            }
        }

        jobs.awaitAll().filterNotNull()
    }


    private fun testTask(name : String, students: List<Pair<Groups.Group.Student, File>>) : List<Pair<Groups.Group.Student, Triple<Int, Int, Int>>> {
        val runtime = Runtime.getRuntime()
        return students.mapNotNull { student ->
            try {
                val dir = File(student.second.absolutePath + File.separator + name)
                val args : List<String>
                val ret : Int

                args = listOf("./gradlew", "test")

                ret = ProcessBuilder()
                    .command(args)
                    .directory(dir)
                    .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                    .redirectError(ProcessBuilder.Redirect.DISCARD)
                    .start().waitFor()

                val resDir = File(student.second.absolutePath + File.separator + name + File.separator
                        + "build" + File.separator + "test-results" + File.separator + "test")
                var success = 0
                var skipped = 0
                var failure = 0
                for (file in resDir.listFiles()!!) {
                    if (file.toString().contains("TEST-")) {
                        val res = parseTestResults(file)
                        success += res.first
                        failure += res.second
                        skipped += res.third
                    }
                }
                Pair(student.first, Triple(success, failure, skipped))
            } catch (e : IOException) {
                System.err.println("error: ${e.message}")
                null
            } catch (e : NullPointerException) {
                System.err.println("error: " + e.message)
                null
            }
        }
    }

    private fun parseTestResults(xmlContent: File): Triple<Int, Int, Int> {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val document = builder.parse(xmlContent)
        val testSuite = document.documentElement

        val total = testSuite.getAttribute("tests").toInt()
        val failures = testSuite.getAttribute("failures").toInt()
        val errors = testSuite.getAttribute("errors").toInt()
        val skipped = testSuite.getAttribute("skipped").toInt()

        val failedTests = failures + errors
        val successfulTests = total - failedTests

        return Triple(successfulTests, failedTests, skipped)
    }
}