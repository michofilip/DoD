package dod.game.expression

import dod.game.model.State

abstract class StateExpr extends Expr[State]

object StateExpr {

    final case class Constant(value: State) extends StateExpr

}
