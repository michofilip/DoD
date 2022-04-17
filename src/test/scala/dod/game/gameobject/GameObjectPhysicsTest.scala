package dod.game.gameobject

import dod.game.gameobject.GameObject
import dod.game.gameobject.commons.CommonsProperty
import dod.game.gameobject.physics.PhysicsProperty
import dod.game.gameobject.position.{PositionProperty, PositionTransformer}
import dod.game.gameobject.state.{StateProperty, StateTransformer}
import dod.game.model.Timestamps.Timestamp
import dod.game.model.{Physics, PhysicsSelector, State}
import org.scalatest.funsuite.AnyFunSuite

import java.util.UUID

class GameObjectPhysicsTest extends AnyFunSuite {

    private val stateProperty = StateProperty(state = State.Open, stateTimestamp = Timestamp.zero)
    private val physicsProperty = PhysicsProperty(PhysicsSelector(Some(State.Open) -> Physics(false), Some(State.Closed) -> Physics(true)))
    private val gameObject = GameObject(id = UUID.randomUUID(), name = "TestGameObject", creationTimestamp = Timestamp.zero)
        .withStateProperty(Some(stateProperty))
        .withPhysicsProperty(Some(physicsProperty))


    test("GameObject::physicsAccessor no PhysicsProperty test") {
        val gameObject = GameObject(id = UUID.randomUUID(), name = "TestGameObject", creationTimestamp = Timestamp.zero)

        assertResult(None)(gameObject.physics)
    }

    test("GameObject::physicsAccessor test") {
        assertResult(Some(Physics(solid = false)))(gameObject.physics)
    }

    test("GameObject::physicsAccessor open test") {
        val stateProperty = StateProperty(state = State.Closed, stateTimestamp = Timestamp.zero)
        val gameObject = this.gameObject.withStateProperty(Some(stateProperty))
            .updateState(StateTransformer.open, Timestamp(1000))

        assertResult(Some(Physics(solid = false)))(gameObject.physics)
    }

    test("GameObject::physicsAccessor close test") {
        val stateProperty = StateProperty(state = State.Open, stateTimestamp = Timestamp.zero)
        val gameObject = this.gameObject.withStateProperty(Some(stateProperty))
            .updateState(StateTransformer.close, Timestamp(1000))

        assertResult(Some(Physics(solid = true)))(gameObject.physics)
    }

}
