package dod.game.event

import dod.game.model.Durations.Duration

enum SchedulerEvent extends Event {
    case CheckScheduler(gameObjectId: String, schedulerName: String)
    case ScheduleOnce(gameObjectId: String, schedulerName: String, timerId: String, timerName: String, delay: Duration, events: Seq[Event])
    case ScheduleAtFixedRate(gameObjectId: String, schedulerName: String, timerId: String, timerName: String, delay: Duration, events: Seq[Event])
    case RemoveScheduler(gameObjectId: String, schedulerName: String)
    case DelayScheduler(gameObjectId: String, schedulerName: String, duration: Duration)
}
