package dod.game.event

import dod.game.expression.{StringExpr, TimestampExpr}
import dod.game.model.Timestamp

enum TimerEvent extends Event {
    case AddTimer(gameObjectId: StringExpr, timerName: StringExpr, initialTimestamp: TimestampExpr)
    case AddTimerAndStart(gameObjectId: StringExpr, timerName: StringExpr, initialTimestamp: TimestampExpr)
    case RemoveTimer(gameObjectId: StringExpr, timerName: StringExpr)
    case RemoveAllTimers(gameObjectId: StringExpr)
    case StartTimer(gameObjectId: StringExpr, timerName: StringExpr)
    case StopTimer(gameObjectId: StringExpr, timerName: StringExpr)
    case StartAllTimers(gameObjectId: StringExpr)
    case StopAllTimers(gameObjectId: StringExpr)
}
