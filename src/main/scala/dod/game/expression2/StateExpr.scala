package dod.game.expression2

import dod.game.model.State

object StateExpr {

    final case class Constant(value: State) extends Expr2[State] :
        override def resolve: Option[State] = Some(value)

}
