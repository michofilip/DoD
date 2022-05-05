package dod.game.expression

import dod.game.gameobject.GameObjectRepository
import dod.game.model.{Direction, State}
import dod.game.expression.ExprContext

abstract class StateExpr extends Expr[State]

object StateExpr {

    final case class Constant(value: State) extends StateExpr :
        override def get(using ExprContext): Option[State] = Some(value)

}
