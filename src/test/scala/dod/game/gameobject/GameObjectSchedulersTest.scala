package dod.game.gameobject

import dod.game.gameobject.GameObject
import dod.game.gameobject.commons.CommonsProperty
import dod.game.gameobject.position.{PositionProperty, PositionTransformer}
import dod.game.gameobject.scheduler.{SchedulerProperty, SchedulerTransformer}
import dod.game.gameobject.state.{StateProperty, StateTransformer}
import dod.game.gameobject.timer.{TimersProperty, TimersTransformer}
import dod.game.model
import dod.game.model.Durations.Duration
import dod.game.model.Timestamps.Timestamp
import dod.game.model.{Scheduler, Timer}
import org.scalatest.funsuite.AnyFunSuite

import java.util.UUID
import scala.collection.immutable.Queue

class GameObjectSchedulersTest extends AnyFunSuite {

    private val baseGameObject = GameObject(id = "game_object_id", name = "TestGameObject", creationTimestamp = Timestamp.zero)
    private val gameObject = baseGameObject.withSchedulerProperty()

    test("GameObject::schedulers no SchedulerProperty test") {
        val schedulerName = "scheduler_1"

        assertResult(false)(baseGameObject.scheduler(schedulerName).isDefined)
    }

    test("GameObject::schedulers no scheduler test") {
        val schedulerName = "scheduler_1"

        assertResult(false)(gameObject.scheduler(schedulerName).isDefined)
    }

    test("GameObject::schedulers scheduleOnce test") {
        val schedulerName = "scheduler_1"
        val scheduler = model.Scheduler("game_object_id", "timer_1", Timestamp.zero, Duration.zero, repeating = false, events = Queue.empty)
        val gameObject = this.gameObject
            .updateSchedulers(SchedulerTransformer.scheduleOnce(schedulerName, scheduler.timerId, scheduler.timerKey, scheduler.initialTimeStamp, scheduler.delay, scheduler.events))

        assertResult(true)(gameObject.scheduler(schedulerName).isDefined)

        for {
            schedulerActual <- gameObject.scheduler(schedulerName)
        } yield {
            assertResult(scheduler)(schedulerActual)
        }
    }

    test("GameObject::schedulers scheduleAtFixedRate test") {
        val schedulerName = "scheduler_1"
        val scheduler = model.Scheduler("game_object_id", "timer_1", Timestamp.zero, Duration.zero, repeating = true, events = Queue.empty)
        val gameObject = this.gameObject
            .updateSchedulers(SchedulerTransformer.scheduleAtFixedRate(schedulerName, scheduler.timerId, scheduler.timerKey, scheduler.initialTimeStamp, scheduler.delay, scheduler.events))

        assertResult(true)(gameObject.scheduler(schedulerName).isDefined)

        for {
            schedulerActual <- gameObject.scheduler(schedulerName)
        } yield {
            assertResult(scheduler)(schedulerActual)
        }
    }

    test("GameObject::schedulers removeScheduler test") {
        val schedulerName = "scheduler_1"
        val scheduler = model.Scheduler("game_object_id", "timer_1", Timestamp.zero, Duration.zero, false, Queue.empty)
        val gameObject = this.gameObject
            .updateSchedulers(SchedulerTransformer.scheduleOnce(schedulerName, scheduler.timerId, scheduler.timerKey, scheduler.initialTimeStamp, scheduler.delay, scheduler.events))
            .updateSchedulers(SchedulerTransformer.removeScheduler(schedulerName))

        assertResult(false)(gameObject.scheduler(schedulerName).isDefined)
    }

    test("GameObject::schedulers delaySchedulerBy test") {
        val schedulerName = "scheduler_1"
        val scheduler = model.Scheduler("game_object_id", "timer_1", Timestamp.zero, Duration.zero, false, Queue.empty)
        val schedulerExpected = scheduler.copy(initialTimeStamp = Timestamp(1000))
        val gameObject = this.gameObject
            .updateSchedulers(SchedulerTransformer.scheduleOnce(schedulerName, scheduler.timerId, scheduler.timerKey, scheduler.initialTimeStamp, scheduler.delay, scheduler.events))
            .updateSchedulers(SchedulerTransformer.delaySchedulerBy(schedulerName, Duration(1000)))

        assertResult(true)(gameObject.scheduler(schedulerName).isDefined)

        for {
            schedulerActual <- gameObject.scheduler(schedulerName)
        } yield {
            assertResult(schedulerExpected)(schedulerActual)
        }
    }

}
