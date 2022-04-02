package dod.game.gameobject

import dod.game.gameobject.GameObject
import dod.game.gameobject.commons.CommonsProperty
import dod.game.gameobject.position.{Coordinates, Direction, PositionProperty, PositionTransformer, Shift}
import dod.game.gameobject.state.{State, StateProperty, StateTransformer}
import dod.game.gameobject.timer.{TimersProperty, TimersTransformer}
import dod.game.temporal.Durations.Duration
import dod.game.temporal.Timer
import dod.game.temporal.Timestamps.Timestamp
import org.scalatest.funsuite.AnyFunSuite

import java.util.UUID

class GameObjectTimersTest extends AnyFunSuite {

    private val commonsProperty = CommonsProperty(id = UUID.randomUUID(), name = "TestGameObject", creationTimestamp = Timestamp.zero)

    test("GameObject::timers no TimersProperty test") {
        val key = "timer_1"
        val gameObject = GameObject(commonsProperty = commonsProperty)

        assertResult(false)(gameObject.timer(key).isDefined)
    }

    test("GameObject::timers no timer test") {
        val key = "timer_1"
        val timersProperty = TimersProperty(Map.empty)
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))

        assertResult(false)(gameObject.timer(key).isDefined)
    }

    test("GameObject::timers test") {
        val key = "timer_1"
        val timersProperty = TimersProperty(Map(key -> Timer()))
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))

        assertResult(true)(gameObject.timer(key).isDefined)

        for {
            timer <- gameObject.timer(key)
        } yield {
            assertResult(false)(timer.running)
            assertResult(Timestamp.zero)(timer.timestamp)
            assertResult(Duration.zero)(timer.duration)
            assertResult(Duration.zero)(timer.durationSince(Timestamp.zero))
        }
    }

    test("GameObject::timers addTimer test") {
        val key = "timer_1"
        val timersProperty = TimersProperty(Map.empty)
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.addTimer(key, Timestamp(1000)))

        assertResult(true)(gameObject.timer(key).isDefined)

        for {
            timer <- gameObject.timer(key)
        } yield {
            assertResult(false)(timer.running)
            assertResult(Timestamp(1000))(timer.timestamp)
            assertResult(Duration(1000))(timer.duration)
            assertResult(Duration(1000))(timer.durationSince(Timestamp.zero))
        }
    }

    test("GameObject::timers addTimerAndStart test") {
        val key = "timer_1"
        val timersProperty = TimersProperty(Map.empty)
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.addTimerAndStart(key, Timestamp.zero))

        assertResult(true)(gameObject.timer(key).isDefined)

        for {
            timer <- gameObject.timer(key)
        } yield {
            assertResult(true)(timer.running)
            Thread.sleep(10)

            assert(timer.timestamp > Timestamp.zero)
            assert(timer.duration > Duration.zero)
            assert(timer.durationSince(Timestamp.zero) > Duration.zero)
        }
    }

    test("GameObject::timers removeTimer test") {
        val key = "timer_1"
        val timersProperty = TimersProperty(Map(key -> Timer()))
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.removeTimer(key))

        assertResult(false)(gameObject.timer(key).isDefined)
    }

    test("GameObject::timers removeAllTimers test") {
        val key1 = "timer_1"
        val key2 = "timer_2"
        val timersProperty = TimersProperty(Map(key1 -> Timer(), key2 -> Timer()))
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.removeAllTimers)

        assertResult(false)(gameObject.timer(key1).isDefined)
        assertResult(false)(gameObject.timer(key2).isDefined)
    }

    test("GameObject::timers startTimer test") {
        val key = "timer_1"
        val timersProperty = TimersProperty(Map(key -> Timer()))
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.startTimer(key))

        assertResult(true)(gameObject.timer(key).isDefined)

        for {
            timer <- gameObject.timer(key)
        } yield {
            assertResult(true)(timer.running)
            Thread.sleep(10)

            assert(timer.timestamp > Timestamp.zero)
            assert(timer.duration > Duration.zero)
            assert(timer.durationSince(Timestamp.zero) > Duration.zero)
        }
    }

    test("GameObject::timers stopTimer test") {
        val key = "timer_1"
        val timersProperty = TimersProperty(Map(key -> Timer(running = true)))
        Thread.sleep(10)
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.stopTimer(key))

        assertResult(true)(gameObject.timer(key).isDefined)

        for {
            timer <- gameObject.timer(key)
        } yield {
            assertResult(false)(timer.running)
            assert(timer.timestamp > Timestamp.zero)
            assert(timer.duration > Duration.zero)
            assert(timer.durationSince(Timestamp.zero) > Duration.zero)
        }
    }

    test("GameObject::timers startAllTimers test") {
        val key1 = "timer_1"
        val key2 = "timer_2"
        val timersProperty = TimersProperty(Map(key1 -> Timer(), key2 -> Timer()))
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.startAllTimers)

        assertResult(true)(gameObject.timer(key1).isDefined)
        assertResult(true)(gameObject.timer(key2).isDefined)

        for {
            timer1 <- gameObject.timer(key1)
            timer2 <- gameObject.timer(key2)
        } yield {
            assertResult(true)(timer1.running)
            assertResult(true)(timer2.running)
            Thread.sleep(10)

            assert(timer1.timestamp > Timestamp.zero)
            assert(timer1.duration > Duration.zero)
            assert(timer1.durationSince(Timestamp.zero) > Duration.zero)

            assert(timer2.timestamp > Timestamp.zero)
            assert(timer2.duration > Duration.zero)
            assert(timer2.durationSince(Timestamp.zero) > Duration.zero)
        }
    }

    test("GameObject::timers stopAllTimers test") {
        val key1 = "timer_1"
        val key2 = "timer_2"
        val timersProperty = TimersProperty(Map(key1 -> Timer(running = true), key2 -> Timer(running = true)))
        Thread.sleep(10)
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.stopAllTimers)

        assertResult(true)(gameObject.timer(key1).isDefined)
        assertResult(true)(gameObject.timer(key2).isDefined)

        for {
            timer1 <- gameObject.timer(key1)
            timer2 <- gameObject.timer(key2)
        } yield {
            assertResult(false)(timer1.running)
            assertResult(false)(timer2.running)

            assert(timer1.timestamp > Timestamp.zero)
            assert(timer1.duration > Duration.zero)
            assert(timer1.durationSince(Timestamp.zero) > Duration.zero)

            assert(timer2.timestamp > Timestamp.zero)
            assert(timer2.duration > Duration.zero)
            assert(timer2.durationSince(Timestamp.zero) > Duration.zero)
        }
    }

}
