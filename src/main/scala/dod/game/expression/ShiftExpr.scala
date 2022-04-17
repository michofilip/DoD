package dod.game.expression

import dod.game.model.Shift

abstract class ShiftExpr extends Expr[Shift]

object ShiftExpr {

    final case class Constant(value: Shift) extends ShiftExpr

}
