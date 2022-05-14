package dod.game.expression

import dod.game.model.Durations.Duration
import dod.game.model.Timestamps.Timestamp
import dod.game.model.{Coordinates, Direction, Shift, State}

object Implicits {

    given Conversion[Boolean, BooleanExpr] = Expr.apply

    given Conversion[Int, IntegerExpr] = Expr.apply

    given Conversion[Double, DecimalExpr] = Expr.apply

    given Conversion[String, StringExpr] = Expr.apply

    given Conversion[Timestamp, TimestampExpr] = Expr.apply

    given Conversion[Duration, DurationExpr] = Expr.apply

    given Conversion[Coordinates, CoordinatesExpr] = Expr.apply

    given Conversion[Shift, ShiftExpr] = Expr.apply

    given Conversion[Direction, DirectionExpr] = Expr.apply

    given Conversion[State, StateExpr] = Expr.apply

}
