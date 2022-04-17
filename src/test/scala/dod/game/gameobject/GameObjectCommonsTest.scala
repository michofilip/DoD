package dod.game.gameobject

import dod.game.gameobject.GameObject
import dod.game.gameobject.commons.CommonsProperty
import dod.game.gameobject.position.{PositionProperty, PositionTransformer}
import dod.game.model.Timestamps.Timestamp
import org.scalatest.funsuite.AnyFunSuite

import java.util.UUID

class GameObjectCommonsTest extends AnyFunSuite {
    private val id = UUID.randomUUID()
    private val gameObject = GameObject(id = id, name = "TestGameObject", creationTimestamp = Timestamp.zero)

    test("GameObject::commonsAccessor test") {
        assertResult(id)(gameObject.id)
        assertResult("TestGameObject")(gameObject.name)
        assertResult(Timestamp.zero)(gameObject.creationTimestamp)
    }
}
