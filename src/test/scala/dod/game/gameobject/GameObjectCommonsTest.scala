package dod.game.gameobject

import dod.game.gameobject.GameObject
import dod.game.gameobject.commons.CommonsProperty
import dod.game.gameobject.position.{Coordinates, Direction, PositionProperty, PositionTransformer, Shift}
import dod.game.temporal.Timestamps.Timestamp
import org.scalatest.funsuite.AnyFunSuite

import java.util.UUID

class GameObjectCommonsTest extends AnyFunSuite {
    private val id = UUID.randomUUID()
    private val commonsProperty = CommonsProperty(id = id, name = "TestGameObject", creationTimestamp = Timestamp.zero)
    private val gameObject = GameObject(commonsProperty = commonsProperty)

    test("GameObject::commonsAccessor test") {
        assertResult(id)(gameObject.id)
        assertResult("TestGameObject")(gameObject.name)
        assertResult(Timestamp.zero)(gameObject.creationTimestamp)
    }
}
