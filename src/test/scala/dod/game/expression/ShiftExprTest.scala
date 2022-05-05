package dod.game.expression

import dod.game.GameStage
import dod.game.gameobject.GameObjectRepository
import dod.game.model.{Coordinates, Shift}
import org.scalatest.funsuite.AnyFunSuite

class ShiftExprTest extends AnyFunSuite {

    given GameStage = new GameStage(GameObjectRepository())

    test("ShiftExpr::Constant test") {
        assertResult(Some(Shift(0, 0))) {
            val expr = Expr(Shift(0, 0))
            expr.get
        }
    }
}
