package dod.gameobject.commons

import dod.gameobject.GameObject
import dod.gameobject.commons.CommonsProperty
import dod.gameobject.position.{Coordinates, Direction, PositionProperty, PositionTransformer, Shift}
import dod.temporal.Timestamps.Timestamp
import org.scalatest.funsuite.AnyFunSuite

import java.util.UUID

class GameObjectCommonsTest extends AnyFunSuite {

    private val commonsProperty = new CommonsProperty(name = "TestGameObject", creationTimestamp = Timestamp(0))
    private val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty)

    test("GameObject::commonsAccessor test") {
        assertResult("TestGameObject")(gameObject.commonsAccessor.name)
        assertResult(Timestamp(0))(gameObject.commonsAccessor.creationTimestamp)
    }
}
