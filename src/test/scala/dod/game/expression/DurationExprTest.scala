package dod.game.expression

import dod.game.GameStage
import dod.game.gameobject.GameObjectRepository
import dod.game.model.Duration
import dod.game.model.Timestamp
import dod.game.model.{Coordinates, Shift}
import org.scalatest.funsuite.AnyFunSuite

class DurationExprTest extends AnyFunSuite {

    given GameStage = GameStage(GameObjectRepository())

    test("DurationExpr::Constant test") {
        assertResult(Some(Duration.zero)) {
            val expr = Expr(Duration.zero)
            expr.get
        }
    }
}
