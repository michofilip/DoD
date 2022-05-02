package dod.game.gameobject.scheduler

import dod.game.event.Event
import dod.game.model
import dod.game.model.{Scheduler, Timer}
import dod.game.model.Durations.Duration
import dod.game.model.Timestamps.Timestamp

import java.util.UUID
import scala.collection.immutable.Queue
import scala.util.chaining.scalaUtilChainingOps

trait SchedulerTransformer extends (Map[String, Scheduler] => Map[String, Scheduler])

object SchedulerTransformer {

    def scheduleOnce(schedulerName: String, timerId: String, timerName: String, initialTimeStamp: Timestamp, delay: Duration, events: Queue[Event]): SchedulerTransformer =
        schedulers => schedulers + (schedulerName -> model.Scheduler(timerId, timerName, initialTimeStamp, delay, repeating = false, events))

    def scheduleAtFixedRate(schedulerName: String, timerId: String, timerName: String, initialTimeStamp: Timestamp, delay: Duration, events: Queue[Event]): SchedulerTransformer =
        schedulers => schedulers + (schedulerName -> model.Scheduler(timerId, timerName, initialTimeStamp, delay, repeating = true, events))

    def removeScheduler(schedulerName: String): SchedulerTransformer = schedulers => schedulers - schedulerName

    def delaySchedulerBy(schedulerName: String, duration: Duration): SchedulerTransformer =
        schedulers => schedulers.get(schedulerName) match {
            case Some(scheduler) => schedulers + (schedulerName -> scheduler.delayBy(duration))
            case None => schedulers
        }

}