package dod.game.expression

import dod.game.model.Direction

abstract class DirectionExpr extends Expr[Direction]

object DirectionExpr {

    final case class Constant(value: Direction) extends DirectionExpr

}
