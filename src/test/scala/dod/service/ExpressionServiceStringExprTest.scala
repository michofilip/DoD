package dod.service

import dod.game.expression.{BooleanExpr, Expr}
import org.scalatest.funsuite.AnyFunSuite

class ExpressionServiceStringExprTest extends AnyFunSuite {

    private val expressionService = new ExpressionService

    test("StringExpr::Constant test") {
        assertResult(Some("string")) {
            expressionService.resolve {
                Expr("string")
            }
        }
    }

    test("StringExpr::Concatenate test") {
        assertResult(Some("HelloWorld")) {
            expressionService.resolve {
                Expr("Hello") + Expr("World")
            }
        }
    }

    test("StringExpr::IntegerToString test") {
        assertResult(Some("3")) {
            expressionService.resolve {
                Expr(3).toStringExpr
            }
        }
    }

    test("StringExpr::DecimalToString test") {
        assertResult(Some("3.14")) {
            expressionService.resolve {
                Expr(3.14).toStringExpr
            }
        }
    }

}
