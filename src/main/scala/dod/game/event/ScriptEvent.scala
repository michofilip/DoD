package dod.game.event

import dod.game.expression.StringExpr

enum ScriptEvent extends Event {
    case RunScript(gameObjectId: StringExpr, scriptName: StringExpr, lineNo: Int = 0)
}
