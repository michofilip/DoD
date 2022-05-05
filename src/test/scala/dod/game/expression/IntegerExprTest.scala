package dod.game.expression

import dod.game.GameStage
import dod.game.expression.{BooleanExpr, Expr}
import dod.game.gameobject.GameObjectRepository
import org.scalatest.funsuite.AnyFunSuite

class IntegerExprTest extends AnyFunSuite {

    given GameStage = new GameStage(GameObjectRepository())

    test("IntegerExpr::Constant test") {
        assertResult(Some(0)) {
            val expr = Expr(0)
            expr.get
        }
    }

    test("IntegerExpr::Negation test") {
        assertResult(Some(-1)) {
            val expr = -Expr(1)
            expr.get
        }
    }

    test("IntegerExpr::Addition test") {
        assertResult(Some(5)) {
            val expr = Expr(2) + Expr(3)
            expr.get
        }
    }

    test("IntegerExpr::Subtraction test") {
        assertResult(Some(-1)) {
            val expr = Expr(2) - Expr(3)
            expr.get
        }
    }

    test("IntegerExpr::Multiplication test") {
        assertResult(Some(6)) {
            val expr = Expr(2) * Expr(3)
            expr.get
        }
    }

    test("IntegerExpr::Division test") {
        assertResult(Some(2)) {
            val expr = Expr(6) / Expr(3)
            expr.get
        }
        assertResult(Some(2)) {
            val expr = Expr(7) / Expr(3)
            expr.get
        }
        assertResult(None) {
            val expr = Expr(6) / Expr(0)
            expr.get
        }
    }

    test("IntegerExpr::Reminder test") {
        assertResult(Some(1)) {
            val expr = Expr(3) % Expr(2)
            expr.get
        }
        assertResult(None) {
            val expr = Expr(3) % Expr(0)
            expr.get
        }
    }

    test("IntegerExpr::DecimalToInteger test") {
        assertResult(Some(3)) {
            val expr = Expr(3.14).toIntegerExpr
            expr.get
        }
    }

}
