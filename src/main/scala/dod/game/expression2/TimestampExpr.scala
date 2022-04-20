package dod.game.expression2

import dod.game.expression.TimestampExpr
import dod.game.model.Timestamps.Timestamp

object TimestampExpr {

    given Ordering[Timestamp] with {
        override def compare(x: Timestamp, y: Timestamp): Int = java.lang.Long.compare(x.value, y.value)
    }

    final case class Constant(value: Timestamp) extends Expr2[Timestamp] :
        override def resolve: Option[Timestamp] = Some(value)

}
