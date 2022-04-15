package dod.game.gameobject.script

import dod.game.model.Script

trait ScriptTransformer extends (Map[String, Script] => Map[String, Script])

object ScriptTransformer {

    def addScript(scriptName: String, script: Script): ScriptTransformer =
        scripts => scripts + (scriptName -> script)

    def removeScript(scriptName: String): ScriptTransformer =
        scripts => scripts - scriptName

}
