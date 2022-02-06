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


    test("GameObject::graphicsData no GraphicsProperty test") {
        val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty)

        assertResult(None)(gameObject.graphicsData.level)
        assertResult(None)(gameObject.graphicsData.length)
        assertResult(None)(gameObject.graphicsData.frame(Timestamp(0)))
    }

    test("GameObject::graphicsData test") {
        assertResult(Some(0))(gameObject.graphicsData.level)
        assertResult(Some(Duration(1000)))(gameObject.graphicsData.length)
        assertResult(Some(Frame(0, 0, 0)))(gameObject.graphicsData.frame(Timestamp(0)))
    }

    test("GameObject::graphicsData open test") {
        val stateProperty = new StateProperty(state = State.Closed, stateTimestamp = Timestamp(0))
        val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty, stateProperty = Some(stateProperty), graphicsProperty = Some(graphicsProperty))
            .updateState(StateTransformer.open, Timestamp(1000))

        assertResult(Some(0))(gameObject.graphicsData.level)
        assertResult(Some(Duration(1000)))(gameObject.graphicsData.length)
        assertResult(Some(Frame(0, 0, 0)))(gameObject.graphicsData.frame(Timestamp(1000)))
        assertResult(Some(Frame(3, 0, 0)))(gameObject.graphicsData.frame(Timestamp(2000)))
    }

    test("GameObject::graphicsData close test") {
        val stateProperty = new StateProperty(state = State.Open, stateTimestamp = Timestamp(0))
        val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty, stateProperty = Some(stateProperty), graphicsProperty = Some(graphicsProperty))
            .updateState(StateTransformer.close, Timestamp(1000))

        assertResult(Some(0))(gameObject.graphicsData.level)
        assertResult(Some(Duration(1000)))(gameObject.graphicsData.length)
        assertResult(Some(Frame(4, 0, 0)))(gameObject.graphicsData.frame(Timestamp(1000)))
        assertResult(Some(Frame(4, 0, 0)))(gameObject.graphicsData.frame(Timestamp(2000)))
    }

}
