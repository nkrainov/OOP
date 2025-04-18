package org.example

import java.io.File
import java.io.Reader
import javax.script.ScriptEngineManager

class Config {
    val tasks : Tasks = Tasks()
    val groups : Groups = Groups()


    //обработка ошибок!!!!!!!!!!!!!!
    fun readConfig() {

        var reader: Reader? = File("check.checker").reader();
        val engine = ScriptEngineManager().getEngineByExtension("kts")!!

        engine.put("tasks", tasks)
        engine.put("groups", groups)

        engine.eval(reader)

        println(tasks.tasks.first.name)
    }
}