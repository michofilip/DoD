package dod.game.event

import dod.game.gameobject.state.StateTransformer
import dod.game.model.Durations.Duration

import java.util.UUID

enum SchedulerEvent extends Event {
    case CheckScheduler(gameObjectId: UUID, schedulerName: String)
    case ScheduleOnce(gameObjectId: UUID, schedulerName: String, timerId: UUID, timerName: String, delay: Duration, events: Seq[Event])
    case ScheduleAtFixedRate(gameObjectId: UUID, schedulerName: String, timerId: UUID, timerName: String, delay: Duration, events: Seq[Event])
    case RemoveScheduler(gameObjectId: UUID, schedulerName: String)
    case DelayScheduler(gameObjectId: UUID, schedulerName: String, duration: Duration)
}
