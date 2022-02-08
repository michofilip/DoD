package dod.gameobject.commons

import dod.gameobject.GameObject
import dod.gameobject.commons.CommonsProperty
import dod.gameobject.position.{Coordinates, Direction, PositionProperty, PositionTransformer, Shift}
import dod.temporal.Timestamps.Timestamp
import org.scalatest.funsuite.AnyFunSuite

import java.util.UUID

class GameObjectCommonsTest extends AnyFunSuite {
    private val id = UUID.randomUUID()
    private val commonsProperty = new CommonsProperty(id = id, name = "TestGameObject", creationTimestamp = Timestamp(0))
    private val gameObject = new GameObject(commonsProperty = commonsProperty)

    test("GameObject::commonsAccessor test") {
        assertResult(id)(gameObject.commonsAccessor.id)
        assertResult("TestGameObject")(gameObject.commonsAccessor.name)
        assertResult(Timestamp(0))(gameObject.commonsAccessor.creationTimestamp)
    }
}
