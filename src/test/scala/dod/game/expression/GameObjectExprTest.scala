package dod.game.expression

import dod.game.GameStage
import dod.game.gameobject.expressions.ExpressionsTransformer
import dod.game.gameobject.physics.PhysicsProperty
import dod.game.gameobject.position.PositionProperty
import dod.game.gameobject.state.StateProperty
import dod.game.gameobject.{GameObject, GameObjectRepository}
import dod.game.model.Durations.Duration
import dod.game.model.Timestamps.Timestamp
import dod.game.model.{Coordinates, Direction, Physics, PhysicsSelector, Position, Shift, State}
import org.scalatest.funsuite.AnyFunSuite

import java.util.UUID

class GameObjectExprTest extends AnyFunSuite {

    private val id = "game_object_id"

    val exprName1 = "expr_1"
    val exprName2 = "expr_2"
    val exprName3 = "expr_3"
    val exprName4 = "expr_4"
    val exprName5 = "expr_5"
    val exprName6 = "expr_6"
    val exprName7 = "expr_7"
    val exprName8 = "expr_8"
    val exprName9 = "expr_9"
    val exprName10 = "expr_10"

    private val baseGameObject = GameObject(id = id, name = "TestGameObject", creationTimestamp = Timestamp.zero)
    private val positionProperty = PositionProperty(Position(coordinates = Coordinates(0, 0), direction = Direction.North), positionTimestamp = Timestamp.zero)
    private val stateProperty = StateProperty(state = State.Off, stateTimestamp = Timestamp.zero)
    private val physicsProperty = PhysicsProperty(PhysicsSelector(Some(State.Off) -> Physics(false)))
    private val gameObject = baseGameObject
        .withPositionProperty(positionProperty)
        .withStateProperty(stateProperty)
        .withPhysicsProperty(physicsProperty)
        .withExpressionsProperty()
        .updateExpressions(ExpressionsTransformer.setExpr(exprName1, Expr(true)))
        .updateExpressions(ExpressionsTransformer.setExpr(exprName2, Expr(10)))
        .updateExpressions(ExpressionsTransformer.setExpr(exprName3, Expr(3.14)))
        .updateExpressions(ExpressionsTransformer.setExpr(exprName4, Expr("str")))
        .updateExpressions(ExpressionsTransformer.setExpr(exprName5, Expr(Timestamp.zero)))
        .updateExpressions(ExpressionsTransformer.setExpr(exprName6, Expr(Duration.zero)))
        .updateExpressions(ExpressionsTransformer.setExpr(exprName7, Expr(Coordinates(0, 0))))
        .updateExpressions(ExpressionsTransformer.setExpr(exprName8, Expr(Shift(0, 0))))
        .updateExpressions(ExpressionsTransformer.setExpr(exprName9, Expr(Direction.North)))
        .updateExpressions(ExpressionsTransformer.setExpr(exprName10, Expr(State.Off)))

    given GameStage = GameStage(GameObjectRepository() + gameObject)

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

    test("GameObjectExpr::GetBooleanExpr test") {
        assertResult(Some(true)) {
            val expr = GameObjectExpr.GetBooleanExpr(id, exprName1)
            expr.get
        }
    }

    test("GameObjectExpr::GetIntegerExpr test") {
        assertResult(Some(10)) {
            val expr = GameObjectExpr.GetIntegerExpr(id, exprName2)
            expr.get
        }
    }

    test("GameObjectExpr::GetDecimalExpr test") {
        assertResult(Some(3.14)) {
            val expr = GameObjectExpr.GetDecimalExpr(id, exprName3)
            expr.get
        }
    }

    test("GameObjectExpr::GetStringExpr test") {
        assertResult(Some("str")) {
            val expr = GameObjectExpr.GetStringExpr(id, exprName4)
            expr.get
        }
    }

    test("GameObjectExpr::GetTimestampExpr test") {
        assertResult(Some(Timestamp.zero)) {
            val expr = GameObjectExpr.GetTimestampExpr(id, exprName5)
            expr.get
        }
    }

    test("GameObjectExpr::GetDurationExpr test") {
        assertResult(Some(Duration.zero)) {
            val expr = GameObjectExpr.GetDurationExpr(id, exprName6)
            expr.get
        }
    }

    test("GameObjectExpr::GetCoordinatesExpr test") {
        assertResult(Some(Coordinates(0, 0))) {
            val expr = GameObjectExpr.GetCoordinatesExpr(id, exprName7)
            expr.get
        }
    }

    test("GameObjectExpr::GetShiftExpr test") {
        assertResult(Some(Shift(0, 0))) {
            val expr = GameObjectExpr.GetShiftExpr(id, exprName8)
            expr.get
        }
    }

    test("GameObjectExpr::GetDirectionExpr test") {
        assertResult(Some(Direction.North)) {
            val expr = GameObjectExpr.GetDirectionExpr(id, exprName9)
            expr.get
        }
    }

    test("GameObjectExpr::GetStateExpr test") {
        assertResult(Some(State.Off)) {
            val expr = GameObjectExpr.GetStateExpr(id, exprName10)
            expr.get
        }
    }
}
