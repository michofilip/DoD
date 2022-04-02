package dod.game.gameobject

import dod.game.gameobject.GameObject
import dod.game.gameobject.commons.CommonsProperty
import dod.game.gameobject.physics.PhysicsProperty
import dod.game.gameobject.position.{PositionProperty, PositionTransformer}
import dod.game.gameobject.state.{StateProperty, StateTransformer}
import dod.game.model.{Physics, PhysicsSelector, State}
import dod.game.model.Timestamps.Timestamp
import org.scalatest.funsuite.AnyFunSuite

import java.util.UUID

class GameObjectPhysicsTest extends AnyFunSuite {

    private val commonsProperty = CommonsProperty(id = UUID.randomUUID(), name = "TestGameObject", creationTimestamp = Timestamp.zero)
    private val stateProperty = StateProperty(state = State.Open, stateTimestamp = Timestamp.zero)
    private val physicsProperty = PhysicsProperty(PhysicsSelector(Some(State.Open) -> Physics(false), Some(State.Closed) -> Physics(true)))
    private val gameObject = GameObject(commonsProperty = commonsProperty, stateProperty = Some(stateProperty), physicsProperty = Some(physicsProperty))


    test("GameObject::physicsAccessor no PhysicsProperty test") {
        val gameObject = GameObject(commonsProperty = commonsProperty)

        assertResult(None)(gameObject.physics)
    }

    test("GameObject::physicsAccessor test") {
        assertResult(Some(Physics(solid = false)))(gameObject.physics)
    }

    test("GameObject::physicsAccessor open test") {
        val stateProperty = StateProperty(state = State.Closed, stateTimestamp = Timestamp.zero)
        val gameObject = GameObject(commonsProperty = commonsProperty, stateProperty = Some(stateProperty), physicsProperty = Some(physicsProperty))
            .updateState(StateTransformer.open, Timestamp(1000))

        assertResult(Some(Physics(solid = false)))(gameObject.physics)
    }

    test("GameObject::physicsAccessor close test") {
        val stateProperty = StateProperty(state = State.Open, stateTimestamp = Timestamp.zero)
        val gameObject = GameObject(commonsProperty = commonsProperty, stateProperty = Some(stateProperty), physicsProperty = Some(physicsProperty))
            .updateState(StateTransformer.close, Timestamp(1000))

        assertResult(Some(Physics(solid = true)))(gameObject.physics)
    }

}
