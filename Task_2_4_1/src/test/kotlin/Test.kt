package org.example

import kotlinx.html.InputType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

class CalculatorTest {
    val script = """
        tasks {
            task("Task_2_2_1", 2.0, "07.03.2025", "14.03.2025")
            task("Task_2_3_1", 2.0, "28.03.2025", "04.04.2025")
        }

        groups {
            group("23216") {
                student("Nikita", "nkrainov", "https://github.com/nkrainov/OOP")
                student("Timofei", "ttomilov", "https://github.com/ttomilov/OOP")
            }

        }

        criteria {
            default("2")
            grade("5", 5)
            grade("4", 4)
            grade("3", 3)
        }

        checkTasks {
            task("Task_2_2_1") {
                checkFor("Nikita")
                checkFor("Timofei")
            }

            task("Task_2_3_1") {
                checkFor("Nikita")
                checkFor("Timofei")
            }
        }

        points {
            control("Control week", "21.12.2023")
        }
    """.trimIndent()
    @Test
    fun resultsMustExists() {
        val file = File("check.checker")
        file.printWriter().use { out ->
            out.write(script)
        }

        main(emptyArray<String>())

        assertTrue(File("checker").exists())
        assertTrue(File("checker" + File.separator + "results").exists())
        File("checker" + File.separator + "results" + File.separator).listFiles()?.let { assertFalse(it.isEmpty()) }
        assertTrue(File("checker" + File.separator + "checkstyle").exists())

        file.delete()
    }

    @Test
    fun testParse() {
        val file = File("check.checker")
        file.printWriter().use { out ->
            out.write(script)
        }

        val config = Config()
        config.readConfig()

        config.groups.groups.forEach { group ->
            assertEquals(group.name, "23216")
            group.students.forEach { student ->
                assertTrue(student.name in arrayOf("Nikita", "Timofei"))
            }
        }

        assertEquals(config.criteria.default, "2")
        config.criteria.grades.forEach { grade ->
            assertTrue(grade.first in arrayOf("2", "3", "4", "5"))
            assertEquals(grade.second.toString(), grade.first)
        }

        config.tasks.tasks.forEach { task ->
            assertTrue(task.name in arrayOf("Task_2_2_1", "Task_2_3_1"))
        }
    }


    @Test
    fun testStyler() {

    }

    @Test
    fun testTester() {

    }

    @Test
    fun testDocer() {

    }

    @Test
    fun testBuilder() {

    }

    @Test
    fun testLoader() {

    }
}