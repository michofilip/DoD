package dod.game.gameobject.scheduler

import dod.game.event.Event
import dod.game.temporal.Durations.Duration
import dod.game.temporal.Timestamps.Timestamp

import java.util.UUID

final case class Scheduler(timerId: UUID, timerKey: String, initialTimeStamp: Timestamp, delay: Duration, repeating: Boolean, events: Seq[Event]) {

    def delayBy(duration: Duration): Scheduler = copy(initialTimeStamp = initialTimeStamp + duration)

}
