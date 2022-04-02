package dod.game.event

import dod.game.temporal.Timestamps.Timestamp

import java.util.UUID

enum TimerEvent extends Event {
    case AddTimer(gameObjectId: UUID, timerKey: String, initialTimestamp: Timestamp)
    case AddTimerAndStart(gameObjectId: UUID, timerKey: String, initialTimestamp: Timestamp)
    case RemoveTimer(gameObjectId: UUID, timerKey: String)
    case RemoveAllTimers(gameObjectId: UUID)
    case StartTimer(gameObjectId: UUID, timerKey: String)
    case StopTimer(gameObjectId: UUID, timerKey: String)
    case StartAllTimers(gameObjectId: UUID)
    case StopAllTimers(gameObjectId: UUID)
}
