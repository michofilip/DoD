package dod.game.expression2

import dod.game.model.Direction

object DirectionExpr {

    final case class Constant(value: Direction) extends Expr2[Direction] :
        override def resolve: Option[Direction] = Some(value)

}
