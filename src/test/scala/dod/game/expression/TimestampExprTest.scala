package dod.game.expression

import dod.game.gameobject.GameObjectRepository
import dod.game.model.Timestamps.Timestamp
import dod.game.model.{Coordinates, Shift}
import org.scalatest.funsuite.AnyFunSuite

class TimestampExprTest extends AnyFunSuite {

    given GameObjectRepository = GameObjectRepository()

    test("TimestampExpr::Constant test") {
        assertResult(Some(Timestamp.zero)) {
            val expr = Expr(Timestamp.zero)
            expr.get
        }
    }
}
