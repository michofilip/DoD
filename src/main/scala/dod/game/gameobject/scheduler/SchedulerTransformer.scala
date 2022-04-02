package dod.game.gameobject.scheduler

import dod.game.event.Event
import dod.game.model
import dod.game.model.{Scheduler, Timer}
import dod.game.model.Durations.Duration
import dod.game.model.Timestamps.Timestamp

import java.util.UUID
import scala.util.chaining.scalaUtilChainingOps

trait SchedulerTransformer extends (Map[String, Scheduler] => Map[String, Scheduler])

object SchedulerTransformer {

    def scheduleOnce(schedulerKey: String, timerId: UUID, timerKey: String, initialTimeStamp: Timestamp, delay: Duration, events: Seq[Event]): SchedulerTransformer =
        schedulers => schedulers + (schedulerKey -> model.Scheduler(timerId, timerKey, initialTimeStamp, delay, repeating = false, events))

    def scheduleAtFixedRate(schedulerKey: String, timerId: UUID, timerKey: String, initialTimeStamp: Timestamp, delay: Duration, events: Seq[Event]): SchedulerTransformer =
        schedulers => schedulers + (schedulerKey -> model.Scheduler(timerId, timerKey, initialTimeStamp, delay, repeating = true, events))

    def removeScheduler(schedulerKey: String): SchedulerTransformer = schedulers => schedulers - schedulerKey

    def delaySchedulerBy(schedulerKey: String, duration: Duration): SchedulerTransformer =
        schedulers => schedulers.get(schedulerKey) match {
            case Some(scheduler) => schedulers + (schedulerKey -> scheduler.delayBy(duration))
            case None => schedulers
        }

}