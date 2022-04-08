package dod.service

import dod.game.expression.{BooleanExpr, Expr}
import org.scalatest.funsuite.AnyFunSuite

class ExpressionServiceDecimalExprTest extends AnyFunSuite {

    private val expressionService = new ExpressionService

    test("DecimalExpr::Constant test") {
        assertResult(Some(3.14)) {
            expressionService.resolve {
                Expr(3.14)
            }
        }
    }

    test("DecimalExpr::Negation test") {
        assertResult(Some(-3.14)) {
            expressionService.resolve {
                -Expr(3.14)
            }
        }
    }

    test("DecimalExpr::Addition test") {
        assertResult(Some(5.86)) {
            expressionService.resolve {
                Expr(3.14) + Expr(2.72)
            }
        }
    }

    test("DecimalExpr::Subtraction test") {
        assertResult(Some(1.5)) {
            expressionService.resolve {
                Expr(2.75) - Expr(1.25)
            }
        }
    }

    test("DecimalExpr::Multiplication test") {
        assertResult(Some(3.375)) {
            expressionService.resolve {
                Expr(1.5) * Expr(2.25)
            }
        }
    }

    test("DecimalExpr::Division test") {
        assertResult(Some(1.5)) {
            expressionService.resolve {
                Expr(3.375) / Expr(2.25)
            }
        }
        assertResult(None) {
            expressionService.resolve {
                Expr(3.375) / Expr(0)
            }
        }
    }

    test("DecimalExpr::IntegerToDecimal test") {
        assertResult(Some(3.0)) {
            expressionService.resolve {
                Expr(3).toDecimalExpr
            }
        }
    }

}
