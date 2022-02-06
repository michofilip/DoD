package dod.gameobject.state

import dod.gameobject.GameObject
import dod.gameobject.commons.CommonsProperty
import dod.gameobject.position.{Coordinates, Direction, PositionProperty, PositionTransformer, Shift}
import dod.temporal.Timestamps.Timestamp
import org.scalatest.funsuite.AnyFunSuite

import java.util.UUID

class GameObjectStateTest extends AnyFunSuite {

    private val commonsProperty = new CommonsProperty(name = "TestGameObject", creationTimestamp = Timestamp(0))

    test("GameObject::stateData no StateProperty test") {
        val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty)

        assertResult(None)(gameObject.stateData.state)
        assertResult(None)(gameObject.stateData.stateTimestamp)
    }

    test("GameObject::stateData test") {
        val stateProperty = new StateProperty(state = State.Off, stateTimestamp = Timestamp(0))
        val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty, stateProperty = Some(stateProperty))

        assertResult(Some(State.Off))(gameObject.stateData.state)
        assertResult(Some(Timestamp(0)))(gameObject.stateData.stateTimestamp)
    }

    test("GameObject::updateState switchOff if off test") {
        val stateProperty = new StateProperty(state = State.Off, stateTimestamp = Timestamp(0))
        val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty, stateProperty = Some(stateProperty))
            .updateState(StateTransformer.switchOff, Timestamp(1000))

        assertResult(Some(State.Off))(gameObject.stateData.state)
        assertResult(Some(Timestamp(0)))(gameObject.stateData.stateTimestamp)
    }

    test("GameObject::updateState switchOff if on test") {
        val stateProperty = new StateProperty(state = State.On, stateTimestamp = Timestamp(0))
        val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty, stateProperty = Some(stateProperty))
            .updateState(StateTransformer.switchOff, Timestamp(1000))

        assertResult(Some(State.Off))(gameObject.stateData.state)
        assertResult(Some(Timestamp(1000)))(gameObject.stateData.stateTimestamp)
    }

    test("GameObject::updateState switchOn if off test") {
        val stateProperty = new StateProperty(state = State.Off, stateTimestamp = Timestamp(0))
        val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty, stateProperty = Some(stateProperty))
            .updateState(StateTransformer.switchOn, Timestamp(1000))

        assertResult(Some(State.On))(gameObject.stateData.state)
        assertResult(Some(Timestamp(1000)))(gameObject.stateData.stateTimestamp)
    }

    test("GameObject::updateState switchOn if on test") {
        val stateProperty = new StateProperty(state = State.On, stateTimestamp = Timestamp(0))
        val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty, stateProperty = Some(stateProperty))
            .updateState(StateTransformer.switchOn, Timestamp(1000))

        assertResult(Some(State.On))(gameObject.stateData.state)
        assertResult(Some(Timestamp(0)))(gameObject.stateData.stateTimestamp)
    }

    test("GameObject::updateState switch if off test") {
        val stateProperty = new StateProperty(state = State.Off, stateTimestamp = Timestamp(0))
        val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty, stateProperty = Some(stateProperty))
            .updateState(StateTransformer.switch, Timestamp(1000))

        assertResult(Some(State.On))(gameObject.stateData.state)
        assertResult(Some(Timestamp(1000)))(gameObject.stateData.stateTimestamp)
    }

    test("GameObject::updateState switch if on test") {
        val stateProperty = new StateProperty(state = State.On, stateTimestamp = Timestamp(0))
        val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty, stateProperty = Some(stateProperty))
            .updateState(StateTransformer.switch, Timestamp(1000))

        assertResult(Some(State.Off))(gameObject.stateData.state)
        assertResult(Some(Timestamp(1000)))(gameObject.stateData.stateTimestamp)
    }

    test("GameObject::updateState open if open test") {
        val stateProperty = new StateProperty(state = State.Open, stateTimestamp = Timestamp(0))
        val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty, stateProperty = Some(stateProperty))
            .updateState(StateTransformer.open, Timestamp(1000))

        assertResult(Some(State.Open))(gameObject.stateData.state)
        assertResult(Some(Timestamp(0)))(gameObject.stateData.stateTimestamp)
    }

    test("GameObject::updateState open if closed test") {
        val stateProperty = new StateProperty(state = State.Closed, stateTimestamp = Timestamp(0))
        val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty, stateProperty = Some(stateProperty))
            .updateState(StateTransformer.open, Timestamp(1000))

        assertResult(Some(State.Open))(gameObject.stateData.state)
        assertResult(Some(Timestamp(1000)))(gameObject.stateData.stateTimestamp)
    }

    test("GameObject::updateState close if open test") {
        val stateProperty = new StateProperty(state = State.Open, stateTimestamp = Timestamp(0))
        val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty, stateProperty = Some(stateProperty))
            .updateState(StateTransformer.close, Timestamp(1000))

        assertResult(Some(State.Closed))(gameObject.stateData.state)
        assertResult(Some(Timestamp(1000)))(gameObject.stateData.stateTimestamp)
    }

    test("GameObject::updateState close if closed test") {
        val stateProperty = new StateProperty(state = State.Closed, stateTimestamp = Timestamp(0))
        val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty, stateProperty = Some(stateProperty))
            .updateState(StateTransformer.close, Timestamp(1000))

        assertResult(Some(State.Closed))(gameObject.stateData.state)
        assertResult(Some(Timestamp(0)))(gameObject.stateData.stateTimestamp)
    }
}
