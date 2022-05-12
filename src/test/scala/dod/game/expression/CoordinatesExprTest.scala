package dod.game.expression

import dod.game.GameStage
import dod.game.gameobject.GameObjectRepository
import dod.game.model.{Coordinates, Shift}
import org.scalatest.funsuite.AnyFunSuite

class CoordinatesExprTest extends AnyFunSuite {

    given GameStage = GameStage(GameObjectRepository())

    test("CoordinatesExpr::Constant test") {
        assertResult(Some(Coordinates(0, 0))) {
            val expr = Expr(Coordinates(0, 0))
            expr.get
        }
    }

    test("CoordinatesExpr::MoveBy test") {
        assertResult(Some(Coordinates(1, 0))) {
            val expr = Expr(Coordinates(0, 0)).moveBy(Expr(Shift(1, 0)))
            expr.get
        }
    }
}
