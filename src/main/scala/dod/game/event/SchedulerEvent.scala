package dod.game.event

import dod.game.expression.{DurationExpr, StringExpr}
import dod.game.model.Durations.Duration

enum SchedulerEvent extends Event {
    case CheckScheduler(gameObjectId: StringExpr, schedulerName: StringExpr)
    case ScheduleOnce(gameObjectId: StringExpr, schedulerName: StringExpr, timerId: StringExpr, timerName: StringExpr, delay: DurationExpr, events: Seq[Event])
    case ScheduleAtFixedRate(gameObjectId: StringExpr, schedulerName: StringExpr, timerId: StringExpr, timerName: StringExpr, delay: DurationExpr, events: Seq[Event])
    case RemoveScheduler(gameObjectId: StringExpr, schedulerName: StringExpr)
    case DelayScheduler(gameObjectId: StringExpr, schedulerName: StringExpr, duration: DurationExpr)
}
