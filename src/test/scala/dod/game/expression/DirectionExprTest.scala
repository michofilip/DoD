package dod.game.expression

import dod.game.gameobject.GameObjectRepository
import dod.game.model.{Coordinates, Direction, Shift}
import org.scalatest.funsuite.AnyFunSuite

class DirectionExprTest extends AnyFunSuite {

    given GameObjectRepository = GameObjectRepository()

    test("DirectionExpr::Constant test") {
        assertResult(Some(Direction.North)) {
            val expr = Expr(Direction.North)
            expr.get
        }
        assertResult(Some(Direction.East)) {
            val expr = Expr(Direction.East)
            expr.get
        }
        assertResult(Some(Direction.South)) {
            val expr = Expr(Direction.South)
            expr.get
        }
        assertResult(Some(Direction.West)) {
            val expr = Expr(Direction.West)
            expr.get
        }
    }
}
