package dod.game.expression2

import dod.game.expression2.Includes.*
import dod.game.gameobject.GameObjectRepository
import dod.service.expression.ExpressionService
import org.scalatest.funsuite.AnyFunSuite

class DecimalExprTest extends AnyFunSuite {

    test("DecimalExpr::Constant test") {
        assertResult(Some(3.14)) {
            val expr = Expr2(3.14)
            expr.resolve
        }
    }

    test("DecimalExpr::Negation test") {
        assertResult(Some(-3.14)) {
            val expr = -Expr2(3.14)
            expr.resolve
        }
    }

    test("DecimalExpr::Addition test") {
        assertResult(Some(5.86)) {
            val expr = Expr2(3.14) + Expr2(2.72)
            expr.resolve
        }
    }

    test("DecimalExpr::Subtraction test") {
        assertResult(Some(1.5)) {
            val expr = Expr2(2.75) - Expr2(1.25)
            expr.resolve
        }
    }

    test("DecimalExpr::Multiplication test") {
        assertResult(Some(3.375)) {
            val expr = Expr2(1.5) * Expr2(2.25)
            expr.resolve
        }
    }

    test("DecimalExpr::Division test") {
        assertResult(Some(1.5)) {
            val expr = Expr2(3.375) / Expr2(2.25)
            expr.resolve
        }
        assertResult(None) {
            val expr = Expr2(3.375) / Expr2(0)
            expr.resolve
        }
    }

    test("DecimalExpr::IntegerToDecimal test") {
        assertResult(Some(3.0)) {
            val expr = Expr2(3).toDecimalExpr
            expr.resolve
        }
    }

}
