package dod.game.gameobject

import dod.game.expression.*
import dod.game.gameobject.GameObject
import dod.game.gameobject.commons.CommonsProperty
import dod.game.gameobject.expressions.ExpressionsTransformer
import dod.game.gameobject.position.{PositionProperty, PositionTransformer}
import dod.game.gameobject.state.{StateProperty, StateTransformer}
import dod.game.gameobject.timer.{TimersProperty, TimersTransformer}
import dod.game.model.Durations.Duration
import dod.game.model.Timestamps.Timestamp
import dod.game.model.{Coordinates, Direction, Shift, State, Timer}
import org.scalatest.funsuite.AnyFunSuite

import java.util.UUID
import scala.util.chaining.scalaUtilChainingOps

class GameObjectExpressionsTest extends AnyFunSuite {

    private val baseGameObject = GameObject(id = "game_object_id", name = "TestGameObject", creationTimestamp = Timestamp.zero)
    private val gameObject = baseGameObject.withExpressionsProperty()

    test("GameObject::expressions no ExpressionsProperty test") {
        val exprName = "expr_1"

        assertResult(false)(baseGameObject.expressions.booleanExpr(exprName).isDefined)
        assertResult(false)(baseGameObject.expressions.integerExpr(exprName).isDefined)
        assertResult(false)(baseGameObject.expressions.decimalExpr(exprName).isDefined)
        assertResult(false)(baseGameObject.expressions.stringExpr(exprName).isDefined)
        assertResult(false)(baseGameObject.expressions.timestampExpr(exprName).isDefined)
        assertResult(false)(baseGameObject.expressions.durationExpr(exprName).isDefined)
        assertResult(false)(baseGameObject.expressions.coordinatesExpr(exprName).isDefined)
        assertResult(false)(baseGameObject.expressions.shiftExpr(exprName).isDefined)
        assertResult(false)(baseGameObject.expressions.directionExpr(exprName).isDefined)
        assertResult(false)(baseGameObject.expressions.stateExpr(exprName).isDefined)
    }

    test("GameObject::expressions no expressions test") {
        val exprName = "expr_1"

        assertResult(false)(gameObject.expressions.booleanExpr(exprName).isDefined)
        assertResult(false)(gameObject.expressions.integerExpr(exprName).isDefined)
        assertResult(false)(gameObject.expressions.decimalExpr(exprName).isDefined)
        assertResult(false)(gameObject.expressions.stringExpr(exprName).isDefined)
        assertResult(false)(gameObject.expressions.timestampExpr(exprName).isDefined)
        assertResult(false)(gameObject.expressions.durationExpr(exprName).isDefined)
        assertResult(false)(gameObject.expressions.coordinatesExpr(exprName).isDefined)
        assertResult(false)(gameObject.expressions.shiftExpr(exprName).isDefined)
        assertResult(false)(gameObject.expressions.directionExpr(exprName).isDefined)
        assertResult(false)(gameObject.expressions.stateExpr(exprName).isDefined)
    }

    test("GameObject::expressions setExpr test") {
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
        val gameObject = this.gameObject
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

        assertResult(true)(gameObject.expressions.booleanExpr(exprName1).isDefined)
        assertResult(true)(gameObject.expressions.integerExpr(exprName2).isDefined)
        assertResult(true)(gameObject.expressions.decimalExpr(exprName3).isDefined)
        assertResult(true)(gameObject.expressions.stringExpr(exprName4).isDefined)
        assertResult(true)(gameObject.expressions.timestampExpr(exprName5).isDefined)
        assertResult(true)(gameObject.expressions.durationExpr(exprName6).isDefined)
        assertResult(true)(gameObject.expressions.coordinatesExpr(exprName7).isDefined)
        assertResult(true)(gameObject.expressions.shiftExpr(exprName8).isDefined)
        assertResult(true)(gameObject.expressions.directionExpr(exprName9).isDefined)
        assertResult(true)(gameObject.expressions.stateExpr(exprName10).isDefined)

        assertResult(Some(BooleanExpr.Constant(true)))(gameObject.expressions.booleanExpr(exprName1))
        assertResult(Some(IntegerExpr.Constant(10)))(gameObject.expressions.integerExpr(exprName2))
        assertResult(Some(DecimalExpr.Constant(3.14)))(gameObject.expressions.decimalExpr(exprName3))
        assertResult(Some(StringExpr.Constant("str")))(gameObject.expressions.stringExpr(exprName4))
        assertResult(Some(TimestampExpr.Constant(Timestamp.zero)))(gameObject.expressions.timestampExpr(exprName5))
        assertResult(Some(DurationExpr.Constant(Duration.zero)))(gameObject.expressions.durationExpr(exprName6))
        assertResult(Some(CoordinatesExpr.Constant(Coordinates(0, 0))))(gameObject.expressions.coordinatesExpr(exprName7))
        assertResult(Some(ShiftExpr.Constant(Shift(0, 0))))(gameObject.expressions.shiftExpr(exprName8))
        assertResult(Some(DirectionExpr.Constant(Direction.North)))(gameObject.expressions.directionExpr(exprName9))
        assertResult(Some(StateExpr.Constant(State.Off)))(gameObject.expressions.stateExpr(exprName10))
    }

    test("GameObject::expressions removeExpr test") {
        val exprName = "expr_1"
        val gameObject = this.gameObject
            .updateExpressions(ExpressionsTransformer.setExpr(exprName, Expr(true)))
            .updateExpressions(ExpressionsTransformer.removeExpr(exprName))

        assertResult(false)(gameObject.expressions.booleanExpr(exprName).isDefined)
    }

    test("GameObject::expressions removeAllExpr test") {
        val exprName1 = "expr_1"
        val exprName2 = "expr_2"
        val gameObject = this.gameObject
            .updateExpressions(ExpressionsTransformer.setExpr(exprName1, Expr(true)))
            .updateExpressions(ExpressionsTransformer.setExpr(exprName2, Expr(10)))
            .updateExpressions(ExpressionsTransformer.removeAllExpr)

        assertResult(false)(gameObject.expressions.booleanExpr(exprName1).isDefined)
        assertResult(false)(gameObject.expressions.booleanExpr(exprName2).isDefined)
    }

}
