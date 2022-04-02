package dod.game.gameobject

import dod.game.gameobject.GameObject
import dod.game.gameobject.commons.CommonsProperty
import dod.game.gameobject.position.{Coordinates, Direction, PositionProperty, PositionTransformer, Shift}
import dod.game.gameobject.scheduler.{Scheduler, SchedulerProperty, SchedulerTransformer}
import dod.game.gameobject.state.{State, StateProperty, StateTransformer}
import dod.game.gameobject.timer.{TimersProperty, TimersTransformer}
import dod.game.temporal.Durations.Duration
import dod.game.temporal.Timer
import dod.game.temporal.Timestamps.Timestamp
import org.scalatest.funsuite.AnyFunSuite

import java.util.UUID

class GameObjectSchedulersTest extends AnyFunSuite {

    private val commonsProperty = CommonsProperty(id = UUID.randomUUID(), name = "TestGameObject", creationTimestamp = Timestamp.zero)

    test("GameObject::schedulers no SchedulerProperty test") {
        val key = "scheduler_1"
        val gameObject = GameObject(commonsProperty = commonsProperty)

        assertResult(false)(gameObject.scheduler(key).isDefined)
    }

    test("GameObject::schedulers no scheduler test") {
        val key = "scheduler_1"
        val schedulerProperty = SchedulerProperty(Map.empty)
        val gameObject = GameObject(commonsProperty = commonsProperty, schedulerProperty = Some(schedulerProperty))

        assertResult(false)(gameObject.scheduler(key).isDefined)
    }

    test("GameObject::schedulers test") {
        val key = "scheduler_1"
        val scheduler = Scheduler(UUID.randomUUID(), "timer_1", Timestamp.zero, Duration.zero, false, Seq.empty)
        val schedulerProperty = SchedulerProperty(Map(key -> scheduler))
        val gameObject = GameObject(commonsProperty = commonsProperty, schedulerProperty = Some(schedulerProperty))

        assertResult(true)(gameObject.scheduler(key).isDefined)

        for {
            schedulerActual <- gameObject.scheduler(key)
        } yield {
            assertResult(scheduler)(schedulerActual)
        }
    }

    test("GameObject::schedulers scheduleOnce test") {
        val key = "scheduler_1"
        val scheduler = Scheduler(UUID.randomUUID(), "timer_1", Timestamp.zero, Duration.zero, repeating = false, events = Seq.empty)
        val schedulerProperty = SchedulerProperty(Map.empty)
        val gameObject = GameObject(commonsProperty = commonsProperty, schedulerProperty = Some(schedulerProperty))
            .updateSchedulers(SchedulerTransformer.scheduleOnce(key, scheduler.timerId, scheduler.timerKey, scheduler.initialTimeStamp, scheduler.delay, scheduler.events))

        assertResult(true)(gameObject.scheduler(key).isDefined)

        for {
            schedulerActual <- gameObject.scheduler(key)
        } yield {
            assertResult(scheduler)(schedulerActual)
        }
    }

    test("GameObject::schedulers scheduleAtFixedRate test") {
        val key = "scheduler_1"
        val scheduler = Scheduler(UUID.randomUUID(), "timer_1", Timestamp.zero, Duration.zero, repeating = true, events = Seq.empty)
        val schedulerProperty = SchedulerProperty(Map.empty)
        val gameObject = GameObject(commonsProperty = commonsProperty, schedulerProperty = Some(schedulerProperty))
            .updateSchedulers(SchedulerTransformer.scheduleAtFixedRate(key, scheduler.timerId, scheduler.timerKey, scheduler.initialTimeStamp, scheduler.delay, scheduler.events))

        assertResult(true)(gameObject.scheduler(key).isDefined)

        for {
            schedulerActual <- gameObject.scheduler(key)
        } yield {
            assertResult(scheduler)(schedulerActual)
        }
    }

    test("GameObject::schedulers removeScheduler test") {
        val key = "scheduler_1"
        val scheduler = Scheduler(UUID.randomUUID(), "timer_1", Timestamp.zero, Duration.zero, false, Seq.empty)
        val schedulerProperty = SchedulerProperty(Map(key -> scheduler))
        val gameObject = GameObject(commonsProperty = commonsProperty, schedulerProperty = Some(schedulerProperty))
            .updateSchedulers(SchedulerTransformer.removeScheduler(key))

        assertResult(false)(gameObject.scheduler(key).isDefined)
    }

    test("GameObject::schedulers delaySchedulerBy test") {
        val key = "scheduler_1"
        val scheduler = Scheduler(UUID.randomUUID(), "timer_1", Timestamp.zero, Duration.zero, false, Seq.empty)
        val schedulerExpected = scheduler.copy(initialTimeStamp = Timestamp(1000))
        val schedulerProperty = SchedulerProperty(Map(key -> scheduler))
        val gameObject = GameObject(commonsProperty = commonsProperty, schedulerProperty = Some(schedulerProperty))
            .updateSchedulers(SchedulerTransformer.delaySchedulerBy(key, Duration(1000)))

        assertResult(true)(gameObject.scheduler(key).isDefined)

        for {
            schedulerActual <- gameObject.scheduler(key)
        } yield {
            assertResult(schedulerExpected)(schedulerActual)
        }
    }

}
