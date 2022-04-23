package dod.game.gameobject

import dod.game.gameobject.GameObject
import dod.game.gameobject.commons.CommonsProperty
import dod.game.gameobject.graphics.GraphicsProperty
import dod.game.gameobject.position.{PositionProperty, PositionTransformer}
import dod.game.gameobject.state.{StateProperty, StateTransformer}
import dod.game.model.Durations.Duration
import dod.game.model.Timestamps.Timestamp
import dod.game.model.{Animation, AnimationSelector, Frame, State}
import org.scalatest.funsuite.AnyFunSuite

import java.util.UUID

class GameObjectGraphicsTest extends AnyFunSuite {

    private val stateProperty = StateProperty(state = State.Open, stateTimestamp = Timestamp.zero)
    private val graphicsProperty = GraphicsProperty(animationSelector = AnimationSelector(
        (Some(State.Open), None) -> Animation.SingleRunAnimation(layer = 0, fps = 4, frames = Vector(Frame(0, 0, 0), Frame(1, 0, 0), Frame(2, 0, 0), Frame(3, 0, 0))),
        (Some(State.Closed), None) -> Animation.LoopingAnimation(layer = 0, fps = 4, frames = Vector(Frame(4, 0, 0), Frame(5, 0, 0), Frame(6, 0, 0), Frame(7, 0, 0)))
    ))

    private val baseGameObject = GameObject(id = "game_object_id", name = "TestGameObject", creationTimestamp = Timestamp.zero)

    test("GameObject::graphicsAccessor no GraphicsProperty test") {
        assertResult(None)(baseGameObject.graphics.layer)
        assertResult(None)(baseGameObject.graphics.length)
        assertResult(None)(baseGameObject.graphics.frame(Timestamp.zero))
    }

    test("GameObject::graphicsAccessor test") {
        val gameObject = baseGameObject
            .withStateProperty(stateProperty)
            .withGraphicsProperty(graphicsProperty)

        assertResult(Some(0))(gameObject.graphics.layer)
        assertResult(Some(Duration(1000)))(gameObject.graphics.length)
        assertResult(Some(Frame(0, 0, 0)))(gameObject.graphics.frame(Timestamp.zero))
    }

    test("GameObject::graphicsAccessor open test") {
        val stateProperty = StateProperty(state = State.Closed, stateTimestamp = Timestamp.zero)
        val gameObject = baseGameObject
            .withStateProperty(stateProperty)
            .withGraphicsProperty(graphicsProperty)
            .updateState(StateTransformer.open, Timestamp(1000))

        assertResult(Some(0))(gameObject.graphics.layer)
        assertResult(Some(Duration(1000)))(gameObject.graphics.length)
        assertResult(Some(Frame(0, 0, 0)))(gameObject.graphics.frame(Timestamp(1000)))
        assertResult(Some(Frame(3, 0, 0)))(gameObject.graphics.frame(Timestamp(2000)))
    }

    test("GameObject::graphicsAccessor close test") {
        val stateProperty = StateProperty(state = State.Open, stateTimestamp = Timestamp.zero)
        val gameObject = baseGameObject
            .withStateProperty(stateProperty)
            .withGraphicsProperty(graphicsProperty)
            .updateState(StateTransformer.close, Timestamp(1000))

        assertResult(Some(0))(gameObject.graphics.layer)
        assertResult(Some(Duration(1000)))(gameObject.graphics.length)
        assertResult(Some(Frame(4, 0, 0)))(gameObject.graphics.frame(Timestamp(1000)))
        assertResult(Some(Frame(4, 0, 0)))(gameObject.graphics.frame(Timestamp(2000)))
    }

}
