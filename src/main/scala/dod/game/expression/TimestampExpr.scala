package dod.game.expression

import dod.game.expression.ExprContext
import dod.game.gameobject.GameObjectRepository
import dod.game.model.State
import dod.game.model.Timestamps.Timestamp

abstract class TimestampExpr extends OrderedExpr[Timestamp]

object TimestampExpr {

    final case class Constant(value: Timestamp) extends TimestampExpr :
        override def get(using ExprContext): Option[Timestamp] = Some(value)

}