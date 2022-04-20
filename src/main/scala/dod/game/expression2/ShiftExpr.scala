package dod.game.expression2

import dod.game.model.Shift

object ShiftExpr {

    final case class Constant(value: Shift) extends Expr2[Shift] :
        override def resolve: Option[Shift] = Some(value)

}
