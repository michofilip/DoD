package dod.game.expression

import dod.game.expression.ExprContext
import dod.game.gameobject.GameObjectRepository
import dod.game.model.Direction

abstract class DirectionExpr extends Expr[Direction]

object DirectionExpr {

    final case class Constant(value: Direction) extends DirectionExpr :
        override def get(using ExprContext): Option[Direction] = Some(value)

}
