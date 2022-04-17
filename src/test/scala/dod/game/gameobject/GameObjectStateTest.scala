package dod.game.gameobject

import dod.game.gameobject.GameObject
import dod.game.gameobject.commons.CommonsProperty
import dod.game.gameobject.position.{PositionProperty, PositionTransformer}
import dod.game.gameobject.state.{StateProperty, StateTransformer}
import dod.game.model.State
import dod.game.model.Timestamps.Timestamp
import org.scalatest.funsuite.AnyFunSuite

import java.util.UUID

class GameObjectStateTest extends AnyFunSuite {

    private val baseGameObject = GameObject(id = UUID.randomUUID(), name = "TestGameObject", creationTimestamp = Timestamp.zero)

    test("GameObject::stateAccessor no StateProperty test") {
        assertResult(None)(baseGameObject.states.state)
        assertResult(None)(baseGameObject.states.stateTimestamp)
    }

    test("GameObject::stateAccessor test") {
        val stateProperty = StateProperty(state = State.Off, stateTimestamp = Timestamp.zero)
        val gameObject = baseGameObject.withStateProperty(stateProperty)

        assertResult(Some(State.Off))(gameObject.states.state)
        assertResult(Some(Timestamp.zero))(gameObject.states.stateTimestamp)
    }

    test("GameObject::updateState switchOff if off test") {
        val stateProperty = StateProperty(state = State.Off, stateTimestamp = Timestamp.zero)
        val gameObject = baseGameObject.withStateProperty(stateProperty)
            .updateState(StateTransformer.switchOff, Timestamp(1000))

        assertResult(Some(State.Off))(gameObject.states.state)
        assertResult(Some(Timestamp.zero))(gameObject.states.stateTimestamp)
    }

    test("GameObject::updateState switchOff if on test") {
        val stateProperty = StateProperty(state = State.On, stateTimestamp = Timestamp.zero)
        val gameObject = baseGameObject.withStateProperty(stateProperty)
            .updateState(StateTransformer.switchOff, Timestamp(1000))

        assertResult(Some(State.Off))(gameObject.states.state)
        assertResult(Some(Timestamp(1000)))(gameObject.states.stateTimestamp)
    }

    test("GameObject::updateState switchOn if off test") {
        val stateProperty = StateProperty(state = State.Off, stateTimestamp = Timestamp.zero)
        val gameObject = baseGameObject.withStateProperty(stateProperty)
            .updateState(StateTransformer.switchOn, Timestamp(1000))

        assertResult(Some(State.On))(gameObject.states.state)
        assertResult(Some(Timestamp(1000)))(gameObject.states.stateTimestamp)
    }

    test("GameObject::updateState switchOn if on test") {
        val stateProperty = StateProperty(state = State.On, stateTimestamp = Timestamp.zero)
        val gameObject = baseGameObject.withStateProperty(stateProperty)
            .updateState(StateTransformer.switchOn, Timestamp(1000))

        assertResult(Some(State.On))(gameObject.states.state)
        assertResult(Some(Timestamp.zero))(gameObject.states.stateTimestamp)
    }

    test("GameObject::updateState switch if off test") {
        val stateProperty = StateProperty(state = State.Off, stateTimestamp = Timestamp.zero)
        val gameObject = baseGameObject.withStateProperty(stateProperty)
            .updateState(StateTransformer.switch, Timestamp(1000))

        assertResult(Some(State.On))(gameObject.states.state)
        assertResult(Some(Timestamp(1000)))(gameObject.states.stateTimestamp)
    }

    test("GameObject::updateState switch if on test") {
        val stateProperty = StateProperty(state = State.On, stateTimestamp = Timestamp.zero)
        val gameObject = baseGameObject.withStateProperty(stateProperty)
            .updateState(StateTransformer.switch, Timestamp(1000))

        assertResult(Some(State.Off))(gameObject.states.state)
        assertResult(Some(Timestamp(1000)))(gameObject.states.stateTimestamp)
    }

    test("GameObject::updateState open if open test") {
        val stateProperty = StateProperty(state = State.Open, stateTimestamp = Timestamp.zero)
        val gameObject = baseGameObject.withStateProperty(stateProperty)
            .updateState(StateTransformer.open, Timestamp(1000))

        assertResult(Some(State.Open))(gameObject.states.state)
        assertResult(Some(Timestamp.zero))(gameObject.states.stateTimestamp)
    }

    test("GameObject::updateState open if closed test") {
        val stateProperty = StateProperty(state = State.Closed, stateTimestamp = Timestamp.zero)
        val gameObject = baseGameObject.withStateProperty(stateProperty)
            .updateState(StateTransformer.open, Timestamp(1000))

        assertResult(Some(State.Open))(gameObject.states.state)
        assertResult(Some(Timestamp(1000)))(gameObject.states.stateTimestamp)
    }

    test("GameObject::updateState close if open test") {
        val stateProperty = StateProperty(state = State.Open, stateTimestamp = Timestamp.zero)
        val gameObject = baseGameObject.withStateProperty(stateProperty)
            .updateState(StateTransformer.close, Timestamp(1000))

        assertResult(Some(State.Closed))(gameObject.states.state)
        assertResult(Some(Timestamp(1000)))(gameObject.states.stateTimestamp)
    }

    test("GameObject::updateState close if closed test") {
        val stateProperty = StateProperty(state = State.Closed, stateTimestamp = Timestamp.zero)
        val gameObject = baseGameObject.withStateProperty(stateProperty)
            .updateState(StateTransformer.close, Timestamp(1000))

        assertResult(Some(State.Closed))(gameObject.states.state)
        assertResult(Some(Timestamp.zero))(gameObject.states.stateTimestamp)
    }
}
