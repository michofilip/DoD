package dod.game.expression

import dod.game.GameStage
import dod.game.expression.{BooleanExpr, Expr}
import dod.game.gameobject.GameObjectRepository
import org.scalatest.funsuite.AnyFunSuite

class StringExprTest extends AnyFunSuite {

    given GameStage = GameStage(GameObjectRepository())

    test("StringExpr::Constant test") {
        assertResult(Some("string")) {
            val expr = Expr("string")
            expr.get
        }
    }

    test("StringExpr::Concatenate test") {
        assertResult(Some("HelloWorld")) {
            val expr = Expr("Hello") + Expr("World")
            expr.get
        }
    }

    test("StringExpr::IntegerToString test") {
        assertResult(Some("3")) {
            val expr = Expr(3).toStringExpr
            expr.get
        }
    }

    test("StringExpr::DecimalToString test") {
        assertResult(Some("3.14")) {
            val expr = Expr(3.14).toStringExpr
            expr.get
        }
    }

}
