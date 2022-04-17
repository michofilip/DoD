package dod.game.event

import dod.game.model.Script

import java.util.UUID

enum ScriptEvent extends Event {
    case RunScript(gameObjectId: UUID, scriptName: String, lineNo: Int = 0)
}
