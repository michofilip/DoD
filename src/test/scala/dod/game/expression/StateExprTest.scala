package dod.game.expression

import dod.game.gameobject.GameObjectRepository
import dod.game.model.{Coordinates, Shift, State}
import org.scalatest.funsuite.AnyFunSuite

class StateExprTest extends AnyFunSuite {

    given GameObjectRepository = GameObjectRepository()

    test("StateExpr::Constant test") {
        assertResult(Some(State.Off)) {
            val expr = Expr(State.Off)
            expr.get
        }
        assertResult(Some(State.On)) {
            val expr = Expr(State.On)
            expr.get
        }
        assertResult(Some(State.Open)) {
            val expr = Expr(State.Open)
            expr.get
        }
        assertResult(Some(State.Closed)) {
            val expr = Expr(State.Closed)
            expr.get
        }
    }
}
