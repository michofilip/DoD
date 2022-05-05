package dod.game.expression

import dod.game.expression.ExprContext
import dod.game.gameobject.GameObjectRepository
import dod.game.model.Durations.Duration
import dod.game.model.Timestamps.Timestamp
import dod.game.model.{Coordinates, Direction, Shift, State}

import scala.annotation.targetName

abstract class Expr[T] {

    def get(using ExprContext): Option[T]

    final def ===(that: Expr[T]): BooleanExpr = BooleanExpr.Equals(this, that)

    final def !==(that: Expr[T]): BooleanExpr = BooleanExpr.UnEquals(this, that)

}

object Expr {

    private[expression] def resolve1[T, R](expr: Expr[T])(f: T => Option[R])(using ExprContext): Option[R] =
        for (x <- expr.get; r <- f(x)) yield r

    private[expression] def resolve2[T1, T2, R](expr1: Expr[T1], expr2: Expr[T2])(f: (T1, T2) => Option[R])(using ExprContext): Option[R] =
        for (x <- expr1.get; y <- expr2.get; r <- f(x, y)) yield r


    extension[T, R] (using ExprContext)(expr: Expr[T])
        private[expression] def ~>(f: T => Option[R]): Option[R] = Expr.resolve1(expr)(f)

    extension[T1, T2, R] (using ExprContext)(pair: (Expr[T1], Expr[T2]))
        private[expression] def ~>(f: (T1, T2) => Option[R]): Option[R] = Expr.resolve2(pair._1, pair._2)(f)


    def apply(value: Boolean): BooleanExpr = BooleanExpr.Constant(value)

    def apply(value: Int): IntegerExpr = IntegerExpr.Constant(value)

    def apply(value: Double): DecimalExpr = DecimalExpr.Constant(value)

    def apply(value: String): StringExpr = StringExpr.Constant(value)

    def apply(value: Timestamp): TimestampExpr = TimestampExpr.Constant(value)

    def apply(value: Duration): DurationExpr = DurationExpr.Constant(value)

    def apply(value: Coordinates): CoordinatesExpr = CoordinatesExpr.Constant(value)

    def apply(value: Shift): ShiftExpr = ShiftExpr.Constant(value)

    def apply(value: Direction): DirectionExpr = DirectionExpr.Constant(value)

    def apply(value: State): StateExpr = StateExpr.Constant(value)

}
