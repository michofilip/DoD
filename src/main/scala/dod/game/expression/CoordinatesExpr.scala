package dod.game.expression

import dod.game.expression.CoordinatesExpr.MoveBy
import dod.game.model.Coordinates

abstract class CoordinatesExpr extends Expr[Coordinates] {

    def moveBy(that: ShiftExpr): CoordinatesExpr = MoveBy(this, that)

}

object CoordinatesExpr {

    final case class Constant(value: Coordinates) extends CoordinatesExpr

    final case class MoveBy(expr1: CoordinatesExpr, expr2: ShiftExpr) extends CoordinatesExpr
    
}
