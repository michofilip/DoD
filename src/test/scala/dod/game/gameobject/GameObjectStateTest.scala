package dod.game.gameobject

import dod.game.gameobject.GameObject
import dod.game.gameobject.commons.CommonsProperty
import dod.game.gameobject.position.{Coordinates, Direction, PositionProperty, PositionTransformer, Shift}
import dod.game.gameobject.state.{State, StateProperty, StateTransformer}
import dod.game.temporal.Timestamps.Timestamp
import org.scalatest.funsuite.AnyFunSuite

import java.util.UUID

class GameObjectStateTest extends AnyFunSuite {

    private val commonsProperty = CommonsProperty(id = UUID.randomUUID(), name = "TestGameObject", creationTimestamp = Timestamp.zero)

    test("GameObject::stateAccessor no StateProperty test") {
        val gameObject = GameObject(commonsProperty = commonsProperty)

        assertResult(None)(gameObject.stateAccessor.state)
        assertResult(None)(gameObject.stateAccessor.stateTimestamp)
    }

    test("GameObject::stateAccessor test") {
        val stateProperty = StateProperty(state = State.Off, stateTimestamp = Timestamp.zero)
        val gameObject = GameObject(commonsProperty = commonsProperty, stateProperty = Some(stateProperty))

        assertResult(Some(State.Off))(gameObject.stateAccessor.state)
        assertResult(Some(Timestamp.zero))(gameObject.stateAccessor.stateTimestamp)
    }

    test("GameObject::updateState switchOff if off test") {
        val stateProperty = StateProperty(state = State.Off, stateTimestamp = Timestamp.zero)
        val gameObject = GameObject(commonsProperty = commonsProperty, stateProperty = Some(stateProperty))
            .updateState(StateTransformer.switchOff, Timestamp(1000))

        assertResult(Some(State.Off))(gameObject.stateAccessor.state)
        assertResult(Some(Timestamp.zero))(gameObject.stateAccessor.stateTimestamp)
    }

    test("GameObject::updateState switchOff if on test") {
        val stateProperty = StateProperty(state = State.On, stateTimestamp = Timestamp.zero)
        val gameObject = GameObject(commonsProperty = commonsProperty, stateProperty = Some(stateProperty))
            .updateState(StateTransformer.switchOff, Timestamp(1000))

        assertResult(Some(State.Off))(gameObject.stateAccessor.state)
        assertResult(Some(Timestamp(1000)))(gameObject.stateAccessor.stateTimestamp)
    }

    test("GameObject::updateState switchOn if off test") {
        val stateProperty = StateProperty(state = State.Off, stateTimestamp = Timestamp.zero)
        val gameObject = GameObject(commonsProperty = commonsProperty, stateProperty = Some(stateProperty))
            .updateState(StateTransformer.switchOn, Timestamp(1000))

        assertResult(Some(State.On))(gameObject.stateAccessor.state)
        assertResult(Some(Timestamp(1000)))(gameObject.stateAccessor.stateTimestamp)
    }

    test("GameObject::updateState switchOn if on test") {
        val stateProperty = StateProperty(state = State.On, stateTimestamp = Timestamp.zero)
        val gameObject = GameObject(commonsProperty = commonsProperty, stateProperty = Some(stateProperty))
            .updateState(StateTransformer.switchOn, Timestamp(1000))

        assertResult(Some(State.On))(gameObject.stateAccessor.state)
        assertResult(Some(Timestamp.zero))(gameObject.stateAccessor.stateTimestamp)
    }

    test("GameObject::updateState switch if off test") {
        val stateProperty = StateProperty(state = State.Off, stateTimestamp = Timestamp.zero)
        val gameObject = GameObject(commonsProperty = commonsProperty, stateProperty = Some(stateProperty))
            .updateState(StateTransformer.switch, Timestamp(1000))

        assertResult(Some(State.On))(gameObject.stateAccessor.state)
        assertResult(Some(Timestamp(1000)))(gameObject.stateAccessor.stateTimestamp)
    }

    test("GameObject::updateState switch if on test") {
        val stateProperty = StateProperty(state = State.On, stateTimestamp = Timestamp.zero)
        val gameObject = GameObject(commonsProperty = commonsProperty, stateProperty = Some(stateProperty))
            .updateState(StateTransformer.switch, Timestamp(1000))

        assertResult(Some(State.Off))(gameObject.stateAccessor.state)
        assertResult(Some(Timestamp(1000)))(gameObject.stateAccessor.stateTimestamp)
    }

    test("GameObject::updateState open if open test") {
        val stateProperty = StateProperty(state = State.Open, stateTimestamp = Timestamp.zero)
        val gameObject = GameObject(commonsProperty = commonsProperty, stateProperty = Some(stateProperty))
            .updateState(StateTransformer.open, Timestamp(1000))

        assertResult(Some(State.Open))(gameObject.stateAccessor.state)
        assertResult(Some(Timestamp.zero))(gameObject.stateAccessor.stateTimestamp)
    }

    test("GameObject::updateState open if closed test") {
        val stateProperty = StateProperty(state = State.Closed, stateTimestamp = Timestamp.zero)
        val gameObject = GameObject(commonsProperty = commonsProperty, stateProperty = Some(stateProperty))
            .updateState(StateTransformer.open, Timestamp(1000))

        assertResult(Some(State.Open))(gameObject.stateAccessor.state)
        assertResult(Some(Timestamp(1000)))(gameObject.stateAccessor.stateTimestamp)
    }

    test("GameObject::updateState close if open test") {
        val stateProperty = StateProperty(state = State.Open, stateTimestamp = Timestamp.zero)
        val gameObject = GameObject(commonsProperty = commonsProperty, stateProperty = Some(stateProperty))
            .updateState(StateTransformer.close, Timestamp(1000))

        assertResult(Some(State.Closed))(gameObject.stateAccessor.state)
        assertResult(Some(Timestamp(1000)))(gameObject.stateAccessor.stateTimestamp)
    }

    test("GameObject::updateState close if closed test") {
        val stateProperty = StateProperty(state = State.Closed, stateTimestamp = Timestamp.zero)
        val gameObject = GameObject(commonsProperty = commonsProperty, stateProperty = Some(stateProperty))
            .updateState(StateTransformer.close, Timestamp(1000))

        assertResult(Some(State.Closed))(gameObject.stateAccessor.state)
        assertResult(Some(Timestamp.zero))(gameObject.stateAccessor.stateTimestamp)
    }
}
