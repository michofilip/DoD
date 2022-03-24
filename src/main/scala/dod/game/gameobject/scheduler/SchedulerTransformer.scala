package dod.game.gameobject.scheduler

import dod.game.event.Event
import dod.game.temporal.Durations.Duration
import dod.game.temporal.Timer
import dod.game.temporal.Timestamps.Timestamp

import java.util.UUID
import scala.util.chaining.scalaUtilChainingOps

trait SchedulerTransformer extends (Map[String, Scheduler] => Map[String, Scheduler])

object SchedulerTransformer {

    def scheduleOnce(key: String, delay: Duration, events: Seq[Event]): SchedulerTransformer =
        schedulers => schedulers + (key -> Scheduler(Timer(running = true), delay, repeating = false, events))

    def scheduleAtFixedDelay(key: String, delay: Duration, events: Seq[Event]): SchedulerTransformer =
        schedulers => schedulers + (key -> Scheduler(Timer(running = true), delay, repeating = true, events))

    def removeScheduler(key: String): SchedulerTransformer = schedulers => schedulers - key

    //    def addDelay(key: String, delay: Duration): SchedulerTransformer =
    //        schedulers => schedulers.get(key) match {
    //            case Some(scheduler) =>
    //                scheduler.timer.pipe { timer =>
    //                    Timer(initialTimestamp = timer.timestamp + delay, running = timer.running)
    //                }.pipe { timer =>
    //                    scheduler.copy(timer = timer)
    //                }.pipe { scheduler =>
    //                    schedulers + (key -> scheduler)
    //                }
    //
    //            case None => schedulers
    //        }
    //
    //    def subtractDelay(key: String, delay: Duration): SchedulerTransformer = schedulers => addDelay(key, -delay)(schedulers)

}