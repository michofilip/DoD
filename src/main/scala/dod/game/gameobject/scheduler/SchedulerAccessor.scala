package dod.game.gameobject.scheduler

import dod.game.event.Event
import dod.game.gameobject.scheduler
import dod.game.temporal.Durations.Duration
import dod.game.temporal.Timestamps.Timestamp

import java.util.UUID

trait SchedulerAccessor {
    def scheduler(key: String): Option[Scheduler]
}
