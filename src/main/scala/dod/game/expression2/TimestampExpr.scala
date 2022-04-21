package dod.game.expression2

import dod.game.expression.TimestampExpr
import dod.game.model.Timestamps.Timestamp

object TimestampExpr {

    final case class Constant(value: Timestamp) extends Expr2[Timestamp] :
        override def resolve: Option[Timestamp] = Some(value)

}
