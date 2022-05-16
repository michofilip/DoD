package dod.game.expression

import dod.game.expression.ExprContext
import dod.game.gameobject.GameObjectRepository
import dod.game.model.Duration
import dod.game.model.State
import dod.game.model.Timestamp

abstract class DurationExpr extends OrderedExpr[Duration]

object DurationExpr {

    final case class Constant(value: Duration) extends DurationExpr :
        override def get(using ExprContext): Option[Duration] = Some(value)

}