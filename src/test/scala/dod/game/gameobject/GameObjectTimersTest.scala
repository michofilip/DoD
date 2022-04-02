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

    test("GameObject::timersAccessor no TimersProperty test") {
        val key = "timer_1"
        val gameObject = GameObject(commonsProperty = commonsProperty)

        val timer = gameObject.timersAccessor.timer(key)
        assertResult(false)(timer.isDefined)
    }

    test("GameObject::timersAccessor no timer test") {
        val key = "timer_1"
        val timersProperty = TimersProperty(Map.empty)
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))

        val timer = gameObject.timersAccessor.timer(key)
        assertResult(false)(timer.isDefined)
    }

    test("GameObject::timersAccessor test") {
        val key = "timer_1"
        val timersProperty = TimersProperty(Map(key -> Timer()))
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))

        val timer = gameObject.timersAccessor.timer(key)
        assertResult(true)(timer.isDefined)

        for {
            timer <- timer
        } yield {
            assertResult(false)(timer.running)
            assertResult(Timestamp.zero)(timer.timestamp)
            assertResult(Duration.zero)(timer.duration)
            assertResult(Duration.zero)(timer.durationSince(Timestamp.zero))
        }
    }

    test("GameObject::updateTimers addTimer test") {
        val key = "timer_1"
        val timersProperty = TimersProperty(Map.empty)
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.addTimer(key, Timestamp(1000)))

        val timer = gameObject.timersAccessor.timer(key)
        assertResult(true)(timer.isDefined)

        for {
            timer <- timer
        } yield {
            assertResult(false)(timer.running)
            assertResult(Timestamp(1000))(timer.timestamp)
            assertResult(Duration(1000))(timer.duration)
            assertResult(Duration(1000))(timer.durationSince(Timestamp.zero))
        }
    }

    test("GameObject::updateTimers addTimerAndStart test") {
        val key = "timer_1"
        val timersProperty = TimersProperty(Map.empty)
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.addTimerAndStart(key, Timestamp.zero))

        val timer = gameObject.timersAccessor.timer(key)
        assertResult(true)(timer.isDefined)

        for {
            timer <- timer
        } yield {
            assertResult(true)(timer.running)
            Thread.sleep(10)

            assert(timer.timestamp > Timestamp.zero)
            assert(timer.duration > Duration.zero)
            assert(timer.durationSince(Timestamp.zero) > Duration.zero)
        }
    }

    test("GameObject::timersAccessor removeTimer test") {
        val key = "timer_1"
        val timersProperty = TimersProperty(Map(key -> Timer()))
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.removeTimer(key))

        val timer = gameObject.timersAccessor.timer(key)
        assertResult(false)(timer.isDefined)
    }

    test("GameObject::timersAccessor removeAllTimers test") {
        val key1 = "timer_1"
        val key2 = "timer_2"
        val timersProperty = TimersProperty(Map(key1 -> Timer(), key2 -> Timer()))
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.removeAllTimers)

        val timer1 = gameObject.timersAccessor.timer(key1)
        assertResult(false)(timer1.isDefined)

        val timer2 = gameObject.timersAccessor.timer(key2)
        assertResult(false)(timer2.isDefined)
    }

    test("GameObject::timersAccessor startTimer test") {
        val key = "timer_1"
        val timersProperty = TimersProperty(Map(key -> Timer()))
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.startTimer(key))

        val timer = gameObject.timersAccessor.timer(key)
        assertResult(true)(timer.isDefined)

        for {
            timer <- timer
        } yield {
            assertResult(true)(timer.running)
            Thread.sleep(10)

            assert(timer.timestamp > Timestamp.zero)
            assert(timer.duration > Duration.zero)
            assert(timer.durationSince(Timestamp.zero) > Duration.zero)
        }
    }

    test("GameObject::timersAccessor stopTimer test") {
        val key = "timer_1"
        val timersProperty = TimersProperty(Map(key -> Timer(running = true)))
        Thread.sleep(10)
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.stopTimer(key))

        val timer = gameObject.timersAccessor.timer(key)
        assertResult(true)(timer.isDefined)

        for {
            timer <- timer
        } yield {
            assertResult(false)(timer.running)
            assert(timer.timestamp > Timestamp.zero)
            assert(timer.duration > Duration.zero)
            assert(timer.durationSince(Timestamp.zero) > Duration.zero)
        }
    }

    test("GameObject::timersAccessor startAllTimers test") {
        val key1 = "timer_1"
        val key2 = "timer_2"
        val timersProperty = TimersProperty(Map(key1 -> Timer(), key2 -> Timer()))
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.startAllTimers)

        val timer1 = gameObject.timersAccessor.timer(key1)
        assertResult(true)(timer1.isDefined)
        val timer2 = gameObject.timersAccessor.timer(key2)
        assertResult(true)(timer2.isDefined)

        for {
            timer1 <- timer1
            timer2 <- timer2
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

    test("GameObject::timersAccessor stopAllTimers test") {
        val key1 = "timer_1"
        val key2 = "timer_2"
        val timersProperty = TimersProperty(Map(key1 -> Timer(running = true), key2 -> Timer(running = true)))
        Thread.sleep(10)
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.stopAllTimers)

        val timer1 = gameObject.timersAccessor.timer(key1)
        assertResult(true)(timer1.isDefined)
        val timer2 = gameObject.timersAccessor.timer(key2)
        assertResult(true)(timer2.isDefined)

        for {
            timer1 <- timer1
            timer2 <- timer2
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
