package dod.gameobject.physics

import dod.gameobject.GameObject
import dod.gameobject.commons.CommonsProperty
import dod.gameobject.position.{Coordinates, Direction, PositionProperty, PositionTransformer, Shift}
import dod.gameobject.state.{State, StateProperty, StateTransformer}
import dod.temporal.Timestamps.Timestamp
import org.scalatest.funsuite.AnyFunSuite

import java.util.UUID

class GameObjectPhysicsTest extends AnyFunSuite {

    private val commonsProperty = new CommonsProperty(name = "TestGameObject", creationTimestamp = Timestamp(0))
    private val stateProperty = new StateProperty(state = State.Open, stateTimestamp = Timestamp(0))
    private val physicsProperty = new PhysicsProperty(PhysicsSelector(Some(State.Open) -> Physics(false), Some(State.Closed) -> Physics(true)))
    private val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty, stateProperty = Some(stateProperty), physicsProperty = Some(physicsProperty))


    test("GameObject::physicsAccessor no PhysicsProperty test") {
        val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty)

        assertResult(None)(gameObject.physicsAccessor.physics)
    }

    test("GameObject::physicsAccessor test") {
        assertResult(Some(Physics(solid = false)))(gameObject.physicsAccessor.physics)
    }

    test("GameObject::physicsAccessor open test") {
        val stateProperty = new StateProperty(state = State.Closed, stateTimestamp = Timestamp(0))
        val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty, stateProperty = Some(stateProperty), physicsProperty = Some(physicsProperty))
            .updateState(StateTransformer.open, Timestamp(1000))

        assertResult(Some(Physics(solid = false)))(gameObject.physicsAccessor.physics)
    }

    test("GameObject::physicsAccessor close test") {
        val stateProperty = new StateProperty(state = State.Open, stateTimestamp = Timestamp(0))
        val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty, stateProperty = Some(stateProperty), physicsProperty = Some(physicsProperty))
            .updateState(StateTransformer.close, Timestamp(1000))

        assertResult(Some(Physics(solid = true)))(gameObject.physicsAccessor.physics)
    }

}
