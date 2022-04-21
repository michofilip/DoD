package dod.game.expression

import dod.game.gameobject.GameObjectRepository
import dod.game.model.Shift

abstract class ShiftExpr extends Expr[Shift]

object ShiftExpr {

    final case class Constant(value: Shift) extends ShiftExpr :
        override def get(using GameObjectRepository): Option[Shift] = Some(value)

}
