package dod.game.expression2

import dod.game.model.{Coordinates, Shift}

object CoordinatesExpr {

    extension (expr1: Expr2[Coordinates])
        def moveBy(expr2: Expr2[Shift]): Expr2[Coordinates] = MoveBy(expr1, expr2)


    final case class Constant(value: Coordinates) extends Expr2[Coordinates] :
        override def resolve: Option[Coordinates] = Some(value)

    final case class MoveBy(expr1: Expr2[Coordinates], expr2: Expr2[Shift]) extends Expr2[Coordinates] :
        override def resolve: Option[Coordinates] = (expr1, expr2) ~> ((x, y) => Some(x.moveBy(y)))

}
