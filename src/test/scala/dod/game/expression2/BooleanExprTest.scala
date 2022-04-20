package dod.game.expression2

import dod.game.expression2.Expr2.*
import dod.game.expression2.BooleanExpr.*
import org.scalatest.funsuite.AnyFunSuite

class BooleanExprTest extends AnyFunSuite {

    test("BooleanExpr::Constant test") {
        assertResult(Some(true)) {
            val expr = Expr2(true)
            expr.resolve
        }
        assertResult(Some(false)) {
            val expr = Expr2(false)
            expr.resolve
        }
    }

    test("BooleanExpr::Not test") {
        assertResult(Some(false)) {
            val expr = !Expr2(true)
            expr.resolve
        }
        assertResult(Some(true)) {
            val expr = !Expr2(false)
            expr.resolve
        }
    }

    test("BooleanExpr::And test") {
        assertResult(Some(true)) {
            val expr = Expr2(true) && Expr2(true)
            expr.resolve
        }
        assertResult(Some(false)) {
            val expr = Expr2(true) && Expr2(false)
            expr.resolve
        }
        assertResult(Some(false)) {
            val expr = Expr2(false) && Expr2(true)
            expr.resolve
        }
        assertResult(Some(false)) {
            val expr = Expr2(false) && Expr2(false)
            expr.resolve
        }
    }

    test("BooleanExpr::Or test") {
        assertResult(Some(true)) {
            val expr = Expr2(true) || Expr2(true)
            expr.resolve
        }
        assertResult(Some(true)) {
            val expr = Expr2(true) || Expr2(false)
            expr.resolve
        }
        assertResult(Some(true)) {
            val expr = Expr2(false) || Expr2(true)
            expr.resolve
        }
        assertResult(Some(false)) {
            val expr = Expr2(false) || Expr2(false)
            expr.resolve
        }
    }

    test("BooleanExpr::Equals test") {
        assertResult(Some(true)) {
            val expr = Expr2(true) === Expr2(true)
            expr.resolve
        }
        assertResult(Some(true)) {
            val expr = Expr2(1) === Expr2(1)
            expr.resolve
        }
        assertResult(Some(true)) {
            val expr = Expr2(1.5) === Expr2(1.5)
            expr.resolve
        }
        assertResult(Some(true)) {
            val expr = Expr2("str") === Expr2("str")
            expr.resolve
        }
        assertResult(Some(false)) {
            val expr = Expr2(false) === Expr2(true)
            expr.resolve
        }
        assertResult(Some(false)) {
            val expr = Expr2(0) === Expr2(1)
            expr.resolve
        }
        assertResult(Some(false)) {
            val expr = Expr2(0.5) === Expr2(1.5)
            expr.resolve
        }
        assertResult(Some(false)) {
            val expr = Expr2("rts") === Expr2("str")
            expr.resolve
        }
    }

    test("BooleanExpr::UnEquals test") {
        assertResult(Some(false)) {
            val expr = Expr2(true) !== Expr2(true)
            expr.resolve
        }
        assertResult(Some(false)) {
            val expr = Expr2(1) !== Expr2(1)
            expr.resolve
        }
        assertResult(Some(false)) {
            val expr = Expr2(1.5) !== Expr2(1.5)
            expr.resolve
        }
        assertResult(Some(false)) {
            val expr = Expr2("str") !== Expr2("str")
            expr.resolve
        }
        assertResult(Some(true)) {
            val expr = Expr2(false) !== Expr2(true)
            expr.resolve
        }
        assertResult(Some(true)) {
            val expr = Expr2(0) !== Expr2(1)
            expr.resolve
        }
        assertResult(Some(true)) {
            val expr = Expr2(0.5) !== Expr2(1.5)
            expr.resolve
        }
        assertResult(Some(true)) {
            val expr = Expr2("rts") !== Expr2("str")
            expr.resolve
        }
    }

    test("BooleanExpr::Less test") {
        assertResult(Some(false)) {
            val expr = Expr2(0) < Expr2(0)
            expr.resolve
        }
        assertResult(Some(true)) {
            val expr = Expr2(0) < Expr2(1)
            expr.resolve
        }
        assertResult(Some(false)) {
            val expr = Expr2(1) < Expr2(0)
            expr.resolve
        }
        assertResult(Some(false)) {
            val expr = Expr2(1) < Expr2(1)
            expr.resolve
        }
    }

    test("LessEquals::Less test") {
        assertResult(Some(true)) {
            val expr = Expr2(0) <= Expr2(0)
            expr.resolve
        }
        assertResult(Some(true)) {
            val expr = Expr2(0) <= Expr2(1)
            expr.resolve
        }
        assertResult(Some(false)) {
            val expr = Expr2(1) <= Expr2(0)
            expr.resolve
        }
        assertResult(Some(true)) {
            val expr = Expr2(1) <= Expr2(1)
            expr.resolve
        }
    }

    test("BooleanExpr::Greater test") {
        assertResult(Some(false)) {
            val expr = Expr2(0) > Expr2(0)
            expr.resolve
        }
        assertResult(Some(false)) {
            val expr = Expr2(0) > Expr2(1)
            expr.resolve
        }
        assertResult(Some(true)) {
            val expr = Expr2(1) > Expr2(0)
            expr.resolve
        }
        assertResult(Some(false)) {
            val expr = Expr2(1) > Expr2(1)
            expr.resolve
        }
    }

    test("BooleanExpr::GreaterEquals test") {
        assertResult(Some(true)) {
            val expr = Expr2(0) >= Expr2(0)
            expr.resolve
        }
        assertResult(Some(false)) {
            val expr = Expr2(0) >= Expr2(1)
            expr.resolve
        }
        assertResult(Some(true)) {
            val expr = Expr2(1) >= Expr2(0)
            expr.resolve
        }
        assertResult(Some(true)) {
            val expr = Expr2(1) >= Expr2(1)
            expr.resolve
        }
    }
}
