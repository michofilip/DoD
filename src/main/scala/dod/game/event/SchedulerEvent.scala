package dod.game.event

import dod.game.gameobject.state.StateTransformer
import dod.game.model.Durations.Duration

import java.util.UUID

enum SchedulerEvent extends Event {
    case CheckScheduler(gameObjectId: UUID, schedulerKey: String)
    case ScheduleOnce(gameObjectId: UUID, schedulerKey: String, timerId: UUID, timerKey: String, delay: Duration, events: Seq[Event])
    case ScheduleAtFixedRate(gameObjectId: UUID, schedulerKey: String, timerId: UUID, timerKey: String, delay: Duration, events: Seq[Event])
    case RemoveScheduler(gameObjectId: UUID, schedulerKey: String)
    case DelayScheduler(gameObjectId: UUID, schedulerKey: String, duration: Duration)
}
