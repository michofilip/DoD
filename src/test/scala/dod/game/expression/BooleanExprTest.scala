package dod.game.expression

import dod.game.GameStage
import dod.game.gameobject.GameObjectRepository
import org.scalatest.funsuite.AnyFunSuite

class BooleanExprTest extends AnyFunSuite {

    given GameStage = GameStage(GameObjectRepository())

    test("BooleanExpr::Constant test") {
        assertResult(Some(true)) {
            val expr = Expr(true)
            expr.get
        }
        assertResult(Some(false)) {
            val expr = Expr(false)
            expr.get
        }
    }

    test("BooleanExpr::Not test") {
        assertResult(Some(false)) {
            val expr = !Expr(true)
            expr.get
        }
        assertResult(Some(true)) {
            val expr = !Expr(false)
            expr.get
        }
    }

    test("BooleanExpr::And test") {
        assertResult(Some(true)) {
            val expr = Expr(true) && Expr(true)
            expr.get
        }
        assertResult(Some(false)) {
            val expr = Expr(true) && Expr(false)
            expr.get
        }
        assertResult(Some(false)) {
            val expr = Expr(false) && Expr(true)
            expr.get
        }
        assertResult(Some(false)) {
            val expr = Expr(false) && Expr(false)
            expr.get
        }
    }

    test("BooleanExpr::Or test") {
        assertResult(Some(true)) {
            val expr = Expr(true) || Expr(true)
            expr.get
        }
        assertResult(Some(true)) {
            val expr = Expr(true) || Expr(false)
            expr.get
        }
        assertResult(Some(true)) {
            val expr = Expr(false) || Expr(true)
            expr.get
        }
        assertResult(Some(false)) {
            val expr = Expr(false) || Expr(false)
            expr.get
        }
    }

    test("BooleanExpr::Equals test") {
        assertResult(Some(true)) {
            val expr = Expr(true) === Expr(true)
            expr.get
        }
        assertResult(Some(true)) {
            val expr = Expr(1) === Expr(1)
            expr.get
        }
        assertResult(Some(true)) {
            val expr = Expr(1.5) === Expr(1.5)
            expr.get
        }
        assertResult(Some(true)) {
            val expr = Expr("str") === Expr("str")
            expr.get
        }
        assertResult(Some(false)) {
            val expr = Expr(false) === Expr(true)
            expr.get
        }
        assertResult(Some(false)) {
            val expr = Expr(0) === Expr(1)
            expr.get
        }
        assertResult(Some(false)) {
            val expr = Expr(0.5) === Expr(1.5)
            expr.get
        }
        assertResult(Some(false)) {
            val expr = Expr("rts") === Expr("str")
            expr.get
        }
    }

    test("BooleanExpr::UnEquals test") {
        assertResult(Some(false)) {
            val expr = Expr(true) !== Expr(true)
            expr.get
        }
        assertResult(Some(false)) {
            val expr = Expr(1) !== Expr(1)
            expr.get
        }
        assertResult(Some(false)) {
            val expr = Expr(1.5) !== Expr(1.5)
            expr.get
        }
        assertResult(Some(false)) {
            val expr = Expr("str") !== Expr("str")
            expr.get
        }
        assertResult(Some(true)) {
            val expr = Expr(false) !== Expr(true)
            expr.get
        }
        assertResult(Some(true)) {
            val expr = Expr(0) !== Expr(1)
            expr.get
        }
        assertResult(Some(true)) {
            val expr = Expr(0.5) !== Expr(1.5)
            expr.get
        }
        assertResult(Some(true)) {
            val expr = Expr("rts") !== Expr("str")
            expr.get
        }
    }

    test("BooleanExpr::Less test") {
        assertResult(Some(false)) {
            val expr = Expr(0) < Expr(0)
            expr.get
        }
        assertResult(Some(true)) {
            val expr = Expr(0) < Expr(1)
            expr.get
        }
        assertResult(Some(false)) {
            val expr = Expr(1) < Expr(0)
            expr.get
        }
        assertResult(Some(false)) {
            val expr = Expr(1) < Expr(1)
            expr.get
        }
    }

    test("LessEquals::Less test") {
        assertResult(Some(true)) {
            val expr = Expr(0) <= Expr(0)
            expr.get
        }
        assertResult(Some(true)) {
            val expr = Expr(0) <= Expr(1)
            expr.get
        }
        assertResult(Some(false)) {
            val expr = Expr(1) <= Expr(0)
            expr.get
        }
        assertResult(Some(true)) {
            val expr = Expr(1) <= Expr(1)
            expr.get
        }
    }

    test("BooleanExpr::Greater test") {
        assertResult(Some(false)) {
            val expr = Expr(0) > Expr(0)
            expr.get
        }
        assertResult(Some(false)) {
            val expr = Expr(0) > Expr(1)
            expr.get
        }
        assertResult(Some(true)) {
            val expr = Expr(1) > Expr(0)
            expr.get
        }
        assertResult(Some(false)) {
            val expr = Expr(1) > Expr(1)
            expr.get
        }
    }

    test("BooleanExpr::GreaterEquals test") {
        assertResult(Some(true)) {
            val expr = Expr(0) >= Expr(0)
            expr.get
        }
        assertResult(Some(false)) {
            val expr = Expr(0) >= Expr(1)
            expr.get
        }
        assertResult(Some(true)) {
            val expr = Expr(1) >= Expr(0)
            expr.get
        }
        assertResult(Some(true)) {
            val expr = Expr(1) >= Expr(1)
            expr.get
        }
    }
}
