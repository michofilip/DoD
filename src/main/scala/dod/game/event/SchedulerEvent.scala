package dod.game.event

import dod.game.gameobject.state.{State, StateTransformer}
import dod.game.temporal.Durations.Duration

import java.util.UUID

enum SchedulerEvent extends Event {
    case CheckScheduler(gameObjectId: UUID, schedulerKey: String)
    case ScheduleOnce(gameObjectId: UUID, schedulerKey: String, delay: Duration, events: Seq[Event])
    case ScheduleAtFixedDelay(gameObjectId: UUID, schedulerKey: String, delay: Duration, events: Seq[Event])
    case RemoveScheduler(gameObjectId: UUID, schedulerKey: String)
}
