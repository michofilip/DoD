package dod.game.event

import dod.game.model.Timestamps.Timestamp

import java.util.UUID

enum TimerEvent extends Event {
    case AddTimer(gameObjectId: UUID, timerName: String, initialTimestamp: Timestamp)
    case AddTimerAndStart(gameObjectId: UUID, timerName: String, initialTimestamp: Timestamp)
    case RemoveTimer(gameObjectId: UUID, timerName: String)
    case RemoveAllTimers(gameObjectId: UUID)
    case StartTimer(gameObjectId: UUID, timerName: String)
    case StopTimer(gameObjectId: UUID, timerName: String)
    case StartAllTimers(gameObjectId: UUID)
    case StopAllTimers(gameObjectId: UUID)
}
