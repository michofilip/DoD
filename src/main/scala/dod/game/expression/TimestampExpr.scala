package dod.game.expression

import dod.game.gameobject.GameObjectRepository
import dod.game.model.State
import dod.game.model.Timestamps.Timestamp

abstract class TimestampExpr extends Expr[Timestamp]

object TimestampExpr {

    final case class Constant(value: Timestamp) extends TimestampExpr :
        override def get(using GameObjectRepository): Option[Timestamp] = Some(value)

}