package dod.game.expression

import dod.game.GameStage
import dod.game.gameobject.GameObjectRepository
import org.scalatest.funsuite.AnyFunSuite

class DecimalExprTest extends AnyFunSuite {

    given GameStage = new GameStage(GameObjectRepository())

    test("DecimalExpr::Constant test") {
        assertResult(Some(3.14)) {
            val expr = Expr(3.14)
            expr.get
        }
    }

    test("DecimalExpr::Negation test") {
        assertResult(Some(-3.14)) {
            val expr = -Expr(3.14)
            expr.get
        }
    }

    test("DecimalExpr::Addition test") {
        assertResult(Some(5.86)) {
            val expr = Expr(3.14) + Expr(2.72)
            expr.get
        }
    }

    test("DecimalExpr::Subtraction test") {
        assertResult(Some(1.5)) {
            val expr = Expr(2.75) - Expr(1.25)
            expr.get
        }
    }

    test("DecimalExpr::Multiplication test") {
        assertResult(Some(3.375)) {
            val expr = Expr(1.5) * Expr(2.25)
            expr.get
        }
    }

    test("DecimalExpr::Division test") {
        assertResult(Some(1.5)) {
            val expr = Expr(3.375) / Expr(2.25)
            expr.get
        }
        assertResult(None) {
            val expr = Expr(3.375) / Expr(0)
            expr.get
        }
    }

    test("DecimalExpr::IntegerToDecimal test") {
        assertResult(Some(3.0)) {
            val expr = Expr(3).toDecimalExpr
            expr.get
        }
    }

}
