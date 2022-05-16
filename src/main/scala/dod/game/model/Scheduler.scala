package dod.game.model

import dod.game.event.Event

import java.util.UUID
import scala.collection.immutable.Queue

final case class Scheduler(timerId: String, timerKey: String, initialTimeStamp: Timestamp, delay: Duration, repeating: Boolean, events: Queue[Event]) {

    def delayBy(duration: Duration): Scheduler = copy(initialTimeStamp = initialTimeStamp + duration)

}
