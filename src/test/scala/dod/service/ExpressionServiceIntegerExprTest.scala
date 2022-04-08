package dod.service

import dod.game.expression.{BooleanExpr, Expr}
import org.scalatest.funsuite.AnyFunSuite

class ExpressionServiceIntegerExprTest extends AnyFunSuite {

    private val expressionService = new ExpressionService

    test("IntegerExpr::Constant test") {
        assertResult(Some(0)) {
            expressionService.resolve {
                Expr(0)
            }
        }
    }

    test("IntegerExpr::Negation test") {
        assertResult(Some(-1)) {
            expressionService.resolve {
                -Expr(1)
            }
        }
    }

    test("IntegerExpr::Addition test") {
        assertResult(Some(5)) {
            expressionService.resolve {
                Expr(2) + Expr(3)
            }
        }
    }

    test("IntegerExpr::Subtraction test") {
        assertResult(Some(-1)) {
            expressionService.resolve {
                Expr(2) - Expr(3)
            }
        }
    }

    test("IntegerExpr::Multiplication test") {
        assertResult(Some(6)) {
            expressionService.resolve {
                Expr(2) * Expr(3)
            }
        }
    }

    test("IntegerExpr::Division test") {
        assertResult(Some(2)) {
            expressionService.resolve {
                Expr(6) / Expr(3)
            }
        }
        assertResult(Some(2)) {
            expressionService.resolve {
                Expr(7) / Expr(3)
            }
        }
        assertResult(None) {
            expressionService.resolve {
                Expr(6) / Expr(0)
            }
        }
    }

    test("IntegerExpr::Reminder test") {
        assertResult(Some(1)) {
            expressionService.resolve {
                Expr(3) % Expr(2)
            }
        }
        assertResult(None) {
            expressionService.resolve {
                Expr(3) % Expr(0)
            }
        }
    }

    test("IntegerExpr::DecimalToInteger test") {
        assertResult(Some(3)) {
            expressionService.resolve {
                Expr(3.14).toIntegerExpr
            }
        }
    }

}
