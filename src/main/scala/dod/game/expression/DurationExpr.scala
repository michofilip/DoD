package dod.game.expression

import dod.game.expression.Expr.ExprContext
import dod.game.gameobject.GameObjectRepository
import dod.game.model.Durations.Duration
import dod.game.model.State
import dod.game.model.Timestamps.Timestamp

abstract class DurationExpr extends OrderedExpr[Duration]

object DurationExpr {

    final case class Constant(value: Duration) extends DurationExpr :
        override def get(using ExprContext): Option[Duration] = Some(value)

}