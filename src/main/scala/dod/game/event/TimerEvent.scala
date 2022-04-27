package dod.game.event

import dod.game.expression.TimestampExpr
import dod.game.model.Timestamps.Timestamp

enum TimerEvent extends Event {
    case AddTimer(gameObjectId: String, timerName: String, initialTimestamp: TimestampExpr)
    case AddTimerAndStart(gameObjectId: String, timerName: String, initialTimestamp: TimestampExpr)
    case RemoveTimer(gameObjectId: String, timerName: String)
    case RemoveAllTimers(gameObjectId: String)
    case StartTimer(gameObjectId: String, timerName: String)
    case StopTimer(gameObjectId: String, timerName: String)
    case StartAllTimers(gameObjectId: String)
    case StopAllTimers(gameObjectId: String)
}
