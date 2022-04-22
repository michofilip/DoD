package dod.game.expression

import dod.game.gameobject.GameObjectRepository
import dod.game.model.Shift
import dod.game.expression.Expr.ExprContext

abstract class ShiftExpr extends Expr[Shift]

object ShiftExpr {

    final case class Constant(value: Shift) extends ShiftExpr :
        override def get(using ExprContext): Option[Shift] = Some(value)

}
