package dod.game.event

enum ScriptEvent extends Event {
    case RunScript(gameObjectId: String, scriptName: String, lineNo: Int = 0)
}
