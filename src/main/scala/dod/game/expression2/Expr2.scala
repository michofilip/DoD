package dod.game.expression2

import dod.game.model.Timestamps.Timestamp
import dod.game.model.{Coordinates, Direction, Shift}

import scala.annotation.targetName

abstract class Expr2[T]:
    def resolve: Option[T]


object Expr2 {

    def apply(value: Boolean): Expr2[Boolean] = BooleanExpr.Constant(value)

    def apply(value: Int): Expr2[Int] = IntegerExpr.Constant(value)

    def apply(value: Double): Expr2[Double] = DecimalExpr.Constant(value)

    def apply(value: String): Expr2[String] = StringExpr.Constant(value)

    def apply(value: Timestamp): Expr2[Timestamp] = TimestampExpr.Constant(value)

    def apply(value: Coordinates): Expr2[Coordinates] = CoordinatesExpr.Constant(value)

    def apply(value: Shift): Expr2[Shift] = ShiftExpr.Constant(value)

    def apply(value: Direction): Expr2[Direction] = DirectionExpr.Constant(value)


    private[expression2] def resolve1[T, R](expr: Expr2[T])(f: T => Option[R]): Option[R] =
        for (x <- expr.resolve; r <- f(x)) yield r

    private[expression2] def resolve2[T1, T2, R](expr1: Expr2[T1], expr2: Expr2[T2])(f: (T1, T2) => Option[R]): Option[R] =
        for (x <- expr1.resolve; y <- expr2.resolve; r <- f(x, y)) yield r


    extension[T, R] (expr: Expr2[T])
        private[expression2] def ~>(f: T => Option[R]): Option[R] = Expr2.resolve1(expr)(f)

    extension[T1, T2, R] (pair: (Expr2[T1], Expr2[T2]))
        private[expression2] def ~>(f: (T1, T2) => Option[R]): Option[R] = Expr2.resolve2(pair._1, pair._2)(f)
        
}