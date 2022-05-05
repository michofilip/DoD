package dod.game.expression

import dod.game.GameStage
import dod.game.gameobject.physics.PhysicsProperty
import dod.game.gameobject.position.PositionProperty
import dod.game.gameobject.state.StateProperty
import dod.game.gameobject.{GameObject, GameObjectRepository}
import dod.game.model.Timestamps.Timestamp
import dod.game.model.{Coordinates, Direction, Physics, PhysicsSelector, Position, State}
import org.scalatest.funsuite.AnyFunSuite

import java.util.UUID

class GameObjectExprTest extends AnyFunSuite {

    private val id = "game_object_id"
    private val baseGameObject = GameObject(id = id, name = "TestGameObject", creationTimestamp = Timestamp.zero)
    private val positionProperty = PositionProperty(Position(coordinates = Coordinates(0, 0), direction = Direction.North), positionTimestamp = Timestamp.zero)
    private val stateProperty = StateProperty(state = State.Off, stateTimestamp = Timestamp.zero)
    private val physicsProperty = PhysicsProperty(PhysicsSelector(Some(State.Off) -> Physics(false)))
    private val gameObject = baseGameObject
        .withPositionProperty(positionProperty)
        .withStateProperty(stateProperty)
        .withPhysicsProperty(physicsProperty)

    given GameStage = new GameStage(GameObjectRepository() + gameObject)

    test("GameObjectExpr::GetName test") {
        assertResult(Some("TestGameObject")) {
            val expr = GameObjectExpr.GetName(id)
            expr.get
        }
    }

    test("GameObjectExpr::GetCreationTimestamp test") {
        assertResult(Some(Timestamp.zero)) {
            val expr = GameObjectExpr.GetCreationTimestamp(id)
            expr.get
        }
    }

    test("GameObjectExpr::GetCoordinates test") {
        assertResult(Some(Coordinates(0, 0))) {
            val expr = GameObjectExpr.GetCoordinates(id)
            expr.get
        }
    }

    test("GameObjectExpr::GetDirection test") {
        assertResult(Some(Direction.North)) {
            val expr = GameObjectExpr.GetDirection(id)
            expr.get
        }
    }

    test("GameObjectExpr::GetPositionTimestamp test") {
        assertResult(Some(Timestamp.zero)) {
            val expr = GameObjectExpr.GetPositionTimestamp(id)
            expr.get
        }
    }

    test("GameObjectExpr::GetState test") {
        assertResult(Some(State.Off)) {
            val expr = GameObjectExpr.GetState(id)
            expr.get
        }
    }

    test("GameObjectExpr::GetStateTimestamp test") {
        assertResult(Some(Timestamp.zero)) {
            val expr = GameObjectExpr.GetStateTimestamp(id)
            expr.get
        }
    }

    test("GameObjectExpr::GetSolid test") {
        assertResult(Some(false)) {
            val expr = GameObjectExpr.GetSolid(id)
            expr.get
        }
    }
}
