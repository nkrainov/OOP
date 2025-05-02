package org.example

import com.sun.tools.javac.comp.Check
import jdk.javadoc.internal.doclets.toolkit.util.DocFinder
import java.io.File
import java.io.Reader
import javax.script.ScriptEngineManager

class Config {
    val tasks : Tasks = Tasks()
    val groups : Groups = Groups()
    val checkTasks : CheckTasks = CheckTasks()
    val points : ControlPoints = ControlPoints()
    val criteria : Criteria = Criteria()


    fun readConfig() {

        var reader: Reader? = File("check.checker").reader();
        val engine = ScriptEngineManager().getEngineByExtension("kts")!!

        engine.put("tasks", tasks)
        engine.put("groups", groups)
        engine.put("checkTasks", checkTasks)
        engine.put("points", points)
        engine.put("criteria", criteria)

        engine.eval(reader)
    }
}