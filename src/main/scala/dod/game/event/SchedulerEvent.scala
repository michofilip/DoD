package dod.game.event

import dod.game.expression.{DurationExpr, StringExpr}
import dod.game.model.Duration

import scala.collection.immutable.Queue

enum SchedulerEvent extends Event {
    case CheckScheduler(gameObjectId: StringExpr, schedulerName: StringExpr)
    case ScheduleOnce(gameObjectId: StringExpr, schedulerName: StringExpr, timerId: StringExpr, timerName: StringExpr, delay: DurationExpr, events: Queue[Event])
    case ScheduleAtFixedRate(gameObjectId: StringExpr, schedulerName: StringExpr, timerId: StringExpr, timerName: StringExpr, delay: DurationExpr, events: Queue[Event])
    case RemoveScheduler(gameObjectId: StringExpr, schedulerName: StringExpr)
    case DelayScheduler(gameObjectId: StringExpr, schedulerName: StringExpr, duration: DurationExpr)
}
