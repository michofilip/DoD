package dod.game.expression

import dod.game.gameobject.GameObjectRepository
import dod.game.model.Durations.Duration
import dod.game.model.Timestamps.Timestamp
import dod.game.model.{Coordinates, Shift}
import org.scalatest.funsuite.AnyFunSuite

class DurationExprTest extends AnyFunSuite {

    given GameObjectRepository = GameObjectRepository()

    test("DurationExpr::Constant test") {
        assertResult(Some(Duration.zero)) {
            val expr = Expr(Duration.zero)
            expr.get
        }
    }
}
