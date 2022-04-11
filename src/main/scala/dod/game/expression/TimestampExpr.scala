package dod.game.expression

import dod.game.model.Timestamps.Timestamp

abstract class TimestampExpr extends OrderedExpr[Timestamp] {

    override def compare(x: Timestamp, y: Timestamp): Int = java.lang.Long.compare(x.value, y.value)

}

object TimestampExpr {

    final case class Constant(value: Timestamp) extends TimestampExpr

}