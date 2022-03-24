package dod.game.gameobject.scheduler

import dod.game.event.Event
import dod.game.temporal.Durations.Duration
import dod.game.temporal.Timer
import dod.game.temporal.Timestamps.Timestamp

import java.util.UUID


case class Scheduler(timer: Timer, delay: Duration, repeating: Boolean, events: Seq[Event]) {
    def check: Boolean = timer.duration >= delay
}
