package dod.gameobject

import dod.game.gameobject.GameObject
import dod.game.gameobject.commons.CommonsProperty
import dod.game.gameobject.position.{Coordinates, Direction, PositionProperty, PositionTransformer, Shift}
import dod.game.temporal.Timestamps.Timestamp
import org.scalatest.funsuite.AnyFunSuite

import java.util.UUID

class GameObjectCommonsTest extends AnyFunSuite {
    private val id = UUID.randomUUID()
    private val commonsProperty = CommonsProperty(id = id, name = "TestGameObject", creationTimestamp = Timestamp(0))
    private val gameObject = GameObject(commonsProperty = commonsProperty)

    test("GameObject::commonsAccessor test") {
        assertResult(id)(gameObject.commonsAccessor.id)
        assertResult("TestGameObject")(gameObject.commonsAccessor.name)
        assertResult(Timestamp(0))(gameObject.commonsAccessor.creationTimestamp)
    }
}
