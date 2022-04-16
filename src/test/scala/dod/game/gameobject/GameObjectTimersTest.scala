package dod.game.gameobject

import dod.game.gameobject.GameObject
import dod.game.gameobject.commons.CommonsProperty
import dod.game.gameobject.position.{PositionProperty, PositionTransformer}
import dod.game.gameobject.state.{StateProperty, StateTransformer}
import dod.game.gameobject.timer.{TimersProperty, TimersTransformer}
import dod.game.model.Durations.Duration
import dod.game.model.Timer
import dod.game.model.Timestamps.Timestamp
import org.scalatest.funsuite.AnyFunSuite

import java.util.UUID

class GameObjectTimersTest extends AnyFunSuite {

    private val commonsProperty = CommonsProperty(id = UUID.randomUUID(), name = "TestGameObject", creationTimestamp = Timestamp.zero)

    test("GameObject::timers no TimersProperty test") {
        val timerName = "timer_1"
        val gameObject = GameObject(commonsProperty = commonsProperty)

        assertResult(false)(gameObject.timer(timerName).isDefined)
    }

    test("GameObject::timers no timer test") {
        val timerName = "timer_1"
        val timersProperty = TimersProperty(Map.empty)
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))

        assertResult(false)(gameObject.timer(timerName).isDefined)
    }

    test("GameObject::timers test") {
        val timerName = "timer_1"
        val timersProperty = TimersProperty(Map(timerName -> Timer()))
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))

        assertResult(true)(gameObject.timer(timerName).isDefined)

        for {
            timer <- gameObject.timer(timerName)
        } yield {
            assertResult(false)(timer.running)
            assertResult(Timestamp.zero)(timer.timestamp)
            assertResult(Duration.zero)(timer.duration)
            assertResult(Duration.zero)(timer.durationSince(Timestamp.zero))
        }
    }

    test("GameObject::timers addTimer test") {
        val timerName = "timer_1"
        val timersProperty = TimersProperty(Map.empty)
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.addTimer(timerName, Timestamp(1000)))

        assertResult(true)(gameObject.timer(timerName).isDefined)

        for {
            timer <- gameObject.timer(timerName)
        } yield {
            assertResult(false)(timer.running)
            assertResult(Timestamp(1000))(timer.timestamp)
            assertResult(Duration(1000))(timer.duration)
            assertResult(Duration(1000))(timer.durationSince(Timestamp.zero))
        }
    }

    test("GameObject::timers addTimerAndStart test") {
        val timerName = "timer_1"
        val timersProperty = TimersProperty(Map.empty)
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.addTimerAndStart(timerName, Timestamp.zero))

        assertResult(true)(gameObject.timer(timerName).isDefined)

        for {
            timer <- gameObject.timer(timerName)
        } yield {
            assertResult(true)(timer.running)
            Thread.sleep(10)

            assert(timer.timestamp > Timestamp.zero)
            assert(timer.duration > Duration.zero)
            assert(timer.durationSince(Timestamp.zero) > Duration.zero)
        }
    }

    test("GameObject::timers removeTimer test") {
        val timerName = "timer_1"
        val timersProperty = TimersProperty(Map(timerName -> Timer()))
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.removeTimer(timerName))

        assertResult(false)(gameObject.timer(timerName).isDefined)
    }

    test("GameObject::timers removeAllTimers test") {
        val timerName1 = "timer_1"
        val timerName2 = "timer_2"
        val timersProperty = TimersProperty(Map(timerName1 -> Timer(), timerName2 -> Timer()))
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.removeAllTimers)

        assertResult(false)(gameObject.timer(timerName1).isDefined)
        assertResult(false)(gameObject.timer(timerName2).isDefined)
    }

    test("GameObject::timers startTimer test") {
        val timerName = "timer_1"
        val timersProperty = TimersProperty(Map(timerName -> Timer()))
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.startTimer(timerName))

        assertResult(true)(gameObject.timer(timerName).isDefined)

        for {
            timer <- gameObject.timer(timerName)
        } yield {
            assertResult(true)(timer.running)
            Thread.sleep(10)

            assert(timer.timestamp > Timestamp.zero)
            assert(timer.duration > Duration.zero)
            assert(timer.durationSince(Timestamp.zero) > Duration.zero)
        }
    }

    test("GameObject::timers stopTimer test") {
        val timerName = "timer_1"
        val timersProperty = TimersProperty(Map(timerName -> Timer(running = true)))
        Thread.sleep(10)
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.stopTimer(timerName))

        assertResult(true)(gameObject.timer(timerName).isDefined)

        for {
            timer <- gameObject.timer(timerName)
        } yield {
            assertResult(false)(timer.running)
            assert(timer.timestamp > Timestamp.zero)
            assert(timer.duration > Duration.zero)
            assert(timer.durationSince(Timestamp.zero) > Duration.zero)
        }
    }

    test("GameObject::timers startAllTimers test") {
        val timerName1 = "timer_1"
        val timerName2 = "timer_2"
        val timersProperty = TimersProperty(Map(timerName1 -> Timer(), timerName2 -> Timer()))
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.startAllTimers)

        assertResult(true)(gameObject.timer(timerName1).isDefined)
        assertResult(true)(gameObject.timer(timerName2).isDefined)

        for {
            timer1 <- gameObject.timer(timerName1)
            timer2 <- gameObject.timer(timerName2)
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
        val timerName1 = "timer_1"
        val timerName2 = "timer_2"
        val timersProperty = TimersProperty(Map(timerName1 -> Timer(running = true), timerName2 -> Timer(running = true)))
        Thread.sleep(10)
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.stopAllTimers)

        assertResult(true)(gameObject.timer(timerName1).isDefined)
        assertResult(true)(gameObject.timer(timerName2).isDefined)

        for {
            timer1 <- gameObject.timer(timerName1)
            timer2 <- gameObject.timer(timerName2)
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
