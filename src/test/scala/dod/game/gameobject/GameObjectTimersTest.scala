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

        assertResult(None)(gameObject.timersAccessor.running(key))
        assertResult(None)(gameObject.timersAccessor.timestamp(key))
        assertResult(None)(gameObject.timersAccessor.duration(key))
        assertResult(None)(gameObject.timersAccessor.durationSince(key, Timestamp.zero))
    }

    test("GameObject::timersAccessor no timer test") {
        val key = "timer_1"
        val timersProperty = TimersProperty(Map.empty)
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))

        assertResult(None)(gameObject.timersAccessor.running(key))
        assertResult(None)(gameObject.timersAccessor.timestamp(key))
        assertResult(None)(gameObject.timersAccessor.duration(key))
        assertResult(None)(gameObject.timersAccessor.durationSince(key, Timestamp.zero))
    }

    test("GameObject::timersAccessor test") {
        val key = "timer_1"
        val timersProperty = TimersProperty(Map(key -> Timer()))
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))

        assertResult(Some(false))(gameObject.timersAccessor.running(key))
        assertResult(Some(Timestamp.zero))(gameObject.timersAccessor.timestamp(key))
        assertResult(Some(Duration.zero))(gameObject.timersAccessor.duration(key))
        assertResult(Some(Duration.zero))(gameObject.timersAccessor.durationSince(key, Timestamp.zero))
    }

    test("GameObject::updateTimers addTimer test") {
        val key = "timer_1"
        val timersProperty = TimersProperty(Map.empty)
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.addTimer(key, Timestamp(1000)))

        assertResult(Some(false))(gameObject.timersAccessor.running(key))
        assertResult(Some(Timestamp(1000)))(gameObject.timersAccessor.timestamp(key))
        assertResult(Some(Duration(1000)))(gameObject.timersAccessor.duration(key))
        assertResult(Some(Duration(1000)))(gameObject.timersAccessor.durationSince(key, Timestamp.zero))
    }

    test("GameObject::updateTimers addTimerAndStart test") {
        val key = "timer_1"
        val timersProperty = TimersProperty(Map.empty)
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.addTimerAndStart(key, Timestamp.zero))

        assertResult(Some(true))(gameObject.timersAccessor.running(key))
        Thread.sleep(10)

        assert(gameObject.timersAccessor.timestamp(key).exists(_ > Timestamp.zero))
        assert(gameObject.timersAccessor.duration(key).exists(_ > Duration.zero))
        assert(gameObject.timersAccessor.durationSince(key, Timestamp.zero).exists(_ > Duration.zero))
    }

    test("GameObject::timersAccessor removeTimer test") {
        val key = "timer_1"
        val timersProperty = TimersProperty(Map(key -> Timer()))
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.removeTimer(key))

        assertResult(None)(gameObject.timersAccessor.running(key))
        assertResult(None)(gameObject.timersAccessor.timestamp(key))
        assertResult(None)(gameObject.timersAccessor.duration(key))
        assertResult(None)(gameObject.timersAccessor.durationSince(key, Timestamp.zero))
    }

    test("GameObject::timersAccessor removeAllTimers test") {
        val key1 = "timer_1"
        val key2 = "timer_2"
        val timersProperty = TimersProperty(Map(key1 -> Timer(), key2 -> Timer()))
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.removeAllTimers)

        assertResult(None)(gameObject.timersAccessor.running(key1))
        assertResult(None)(gameObject.timersAccessor.timestamp(key1))
        assertResult(None)(gameObject.timersAccessor.duration(key1))
        assertResult(None)(gameObject.timersAccessor.durationSince(key1, Timestamp.zero))

        assertResult(None)(gameObject.timersAccessor.running(key2))
        assertResult(None)(gameObject.timersAccessor.timestamp(key2))
        assertResult(None)(gameObject.timersAccessor.duration(key2))
        assertResult(None)(gameObject.timersAccessor.durationSince(key2, Timestamp.zero))
    }

    test("GameObject::timersAccessor startTimer test") {
        val key = "timer_1"
        val timersProperty = TimersProperty(Map(key -> Timer()))
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.startTimer(key))

        assertResult(Some(true))(gameObject.timersAccessor.running(key))
        Thread.sleep(10)

        assert(gameObject.timersAccessor.timestamp(key).exists(_ > Timestamp.zero))
        assert(gameObject.timersAccessor.duration(key).exists(_ > Duration.zero))
        assert(gameObject.timersAccessor.durationSince(key, Timestamp.zero).exists(_ > Duration.zero))
    }

    test("GameObject::timersAccessor stopTimer test") {
        val key = "timer_1"
        val timersProperty = TimersProperty(Map(key -> Timer(running = true)))
        Thread.sleep(10)
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.stopTimer(key))

        assertResult(Some(false))(gameObject.timersAccessor.running(key))
        assert(gameObject.timersAccessor.timestamp(key).exists(_ > Timestamp.zero))
        assert(gameObject.timersAccessor.duration(key).exists(_ > Duration.zero))
        assert(gameObject.timersAccessor.durationSince(key, Timestamp.zero).exists(_ > Duration.zero))
    }

    test("GameObject::timersAccessor startAllTimers test") {
        val key1 = "timer_1"
        val key2 = "timer_2"
        val timersProperty = TimersProperty(Map(key1 -> Timer(), key2 -> Timer()))
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.startAllTimers)

        assertResult(Some(true))(gameObject.timersAccessor.running(key1))
        assertResult(Some(true))(gameObject.timersAccessor.running(key2))
        Thread.sleep(10)

        assert(gameObject.timersAccessor.timestamp(key1).exists(_ > Timestamp.zero))
        assert(gameObject.timersAccessor.duration(key1).exists(_ > Duration.zero))
        assert(gameObject.timersAccessor.durationSince(key1, Timestamp.zero).exists(_ > Duration.zero))

        assert(gameObject.timersAccessor.timestamp(key2).exists(_ > Timestamp.zero))
        assert(gameObject.timersAccessor.duration(key2).exists(_ > Duration.zero))
        assert(gameObject.timersAccessor.durationSince(key2, Timestamp.zero).exists(_ > Duration.zero))
    }

    test("GameObject::timersAccessor stopAllTimers test") {
        val key1 = "timer_1"
        val key2 = "timer_2"
        val timersProperty = TimersProperty(Map(key1 -> Timer(running = true), key2 -> Timer(running = true)))
        Thread.sleep(10)
        val gameObject = GameObject(commonsProperty = commonsProperty, timersProperty = Some(timersProperty))
            .updateTimers(TimersTransformer.stopAllTimers)

        assertResult(Some(false))(gameObject.timersAccessor.running(key1))
        assertResult(Some(false))(gameObject.timersAccessor.running(key2))

        assert(gameObject.timersAccessor.timestamp(key1).exists(_ > Timestamp.zero))
        assert(gameObject.timersAccessor.duration(key1).exists(_ > Duration.zero))
        assert(gameObject.timersAccessor.durationSince(key1, Timestamp.zero).exists(_ > Duration.zero))

        assert(gameObject.timersAccessor.timestamp(key2).exists(_ > Timestamp.zero))
        assert(gameObject.timersAccessor.duration(key2).exists(_ > Duration.zero))
        assert(gameObject.timersAccessor.durationSince(key2, Timestamp.zero).exists(_ > Duration.zero))
    }

}
