package dod.game.event

import dod.game.expression.StringExpr

enum StateEvent extends Event {
    case SwitchOff(gameObjectId: StringExpr)
    case SwitchOn(gameObjectId: StringExpr)
    case Switch(gameObjectId: StringExpr)
    case Open(gameObjectId: StringExpr)
    case Close(gameObjectId: StringExpr)
}
