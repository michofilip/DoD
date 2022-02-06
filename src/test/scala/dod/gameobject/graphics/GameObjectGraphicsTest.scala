package dod.gameobject.graphics

import dod.gameobject.GameObject
import dod.gameobject.commons.CommonsProperty
import dod.gameobject.position.{Coordinates, Direction, PositionProperty, PositionTransformer, Shift}
import dod.gameobject.state.{State, StateProperty, StateTransformer}
import dod.temporal.Durations.Duration
import dod.temporal.Timestamps.Timestamp
import org.scalatest.funsuite.AnyFunSuite

import java.util.UUID

class GameObjectGraphicsTest extends AnyFunSuite {

    private val commonsProperty = new CommonsProperty(name = "TestGameObject", creationTimestamp = Timestamp(0))
    private val stateProperty = new StateProperty(state = State.Open, stateTimestamp = Timestamp(0))
    private val graphicsProperty = new GraphicsProperty(0, AnimationSelector(
        (Some(State.Open), None) -> new Animation(4, false, Vector(Frame(0, 0, 0), Frame(1, 0, 0), Frame(2, 0, 0), Frame(3, 0, 0))),
        (Some(State.Closed), None) -> new Animation(4, true, Vector(Frame(4, 0, 0), Frame(5, 0, 0), Frame(6, 0, 0), Frame(7, 0, 0)))
    ))
    private val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty, stateProperty = Some(stateProperty), graphicsProperty = Some(graphicsProperty))


    test("GameObject::graphicsAccessor no GraphicsProperty test") {
        val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty)

        assertResult(None)(gameObject.graphicsAccessor.level)
        assertResult(None)(gameObject.graphicsAccessor.length)
        assertResult(None)(gameObject.graphicsAccessor.frame(Timestamp(0)))
    }

    test("GameObject::graphicsAccessor test") {
        assertResult(Some(0))(gameObject.graphicsAccessor.level)
        assertResult(Some(Duration(1000)))(gameObject.graphicsAccessor.length)
        assertResult(Some(Frame(0, 0, 0)))(gameObject.graphicsAccessor.frame(Timestamp(0)))
    }

    test("GameObject::graphicsAccessor open test") {
        val stateProperty = new StateProperty(state = State.Closed, stateTimestamp = Timestamp(0))
        val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty, stateProperty = Some(stateProperty), graphicsProperty = Some(graphicsProperty))
            .updateState(StateTransformer.open, Timestamp(1000))

        assertResult(Some(0))(gameObject.graphicsAccessor.level)
        assertResult(Some(Duration(1000)))(gameObject.graphicsAccessor.length)
        assertResult(Some(Frame(0, 0, 0)))(gameObject.graphicsAccessor.frame(Timestamp(1000)))
        assertResult(Some(Frame(3, 0, 0)))(gameObject.graphicsAccessor.frame(Timestamp(2000)))
    }

    test("GameObject::graphicsAccessor close test") {
        val stateProperty = new StateProperty(state = State.Open, stateTimestamp = Timestamp(0))
        val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty, stateProperty = Some(stateProperty), graphicsProperty = Some(graphicsProperty))
            .updateState(StateTransformer.close, Timestamp(1000))

        assertResult(Some(0))(gameObject.graphicsAccessor.level)
        assertResult(Some(Duration(1000)))(gameObject.graphicsAccessor.length)
        assertResult(Some(Frame(4, 0, 0)))(gameObject.graphicsAccessor.frame(Timestamp(1000)))
        assertResult(Some(Frame(4, 0, 0)))(gameObject.graphicsAccessor.frame(Timestamp(2000)))
    }

}
