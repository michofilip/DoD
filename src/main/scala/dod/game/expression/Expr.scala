package dod.game.expression

import dod.game.model.Timestamps.Timestamp
import dod.game.model.{Coordinates, Direction, Shift}

abstract class Expr[T] {

    def ===(that: Expr[T]): BooleanExpr = BooleanExpr.Equals(this, that)

    def !==(that: Expr[T]): BooleanExpr = BooleanExpr.UnEquals(this, that)

}

object Expr {

    def apply(value: Boolean): BooleanExpr = BooleanExpr.Constant(value)

    def apply(value: Int): IntegerExpr = IntegerExpr.Constant(value)

    def apply(value: Double): DecimalExpr = DecimalExpr.Constant(value)

    def apply(value: String): StringExpr = StringExpr.Constant(value)

    def apply(value: Timestamp): TimestampExpr = TimestampExpr.Constant(value)

    def apply(value: Coordinates): CoordinatesExpr = CoordinatesExpr.Constant(value)

    def apply(value: Shift): ShiftExpr = ShiftExpr.Constant(value)

    def apply(value: Direction): DirectionExpr = DirectionExpr.Constant(value)

}
