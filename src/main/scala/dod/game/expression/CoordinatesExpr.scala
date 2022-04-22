package dod.game.expression

import dod.game.expression.CoordinatesExpr.MoveBy
import dod.game.expression.Expr.*
import dod.game.gameobject.GameObjectRepository
import dod.game.model.Coordinates

abstract class CoordinatesExpr extends Expr[Coordinates] {

    final def moveBy(that: ShiftExpr): CoordinatesExpr = MoveBy(this, that)

}

object CoordinatesExpr {

    final case class Constant(value: Coordinates) extends CoordinatesExpr :
        override def get(using ExprContext): Option[Coordinates] = Some(value)

    final case class MoveBy(expr1: CoordinatesExpr, expr2: ShiftExpr) extends CoordinatesExpr :
        override def get(using ExprContext): Option[Coordinates] = (expr1, expr2) ~> ((x, y) => Some(x.moveBy(y)))

}
