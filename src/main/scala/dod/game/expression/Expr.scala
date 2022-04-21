package dod.game.expression

import dod.game.gameobject.GameObjectRepository
import dod.game.model.Timestamps.Timestamp
import dod.game.model.{Coordinates, Direction, Shift}

import scala.annotation.targetName

abstract class Expr[T] {

    def get(using GameObjectRepository): Option[T]

    final def ===(that: Expr[T]): BooleanExpr = BooleanExpr.Equals(this, that)

    final def !==(that: Expr[T]): BooleanExpr = BooleanExpr.UnEquals(this, that)

}

object Expr {

    private[expression] def resolve1[T, R](expr: Expr[T])(f: T => Option[R])(using GameObjectRepository): Option[R] =
        for (x <- expr.get; r <- f(x)) yield r

    private[expression] def resolve2[T1, T2, R](expr1: Expr[T1], expr2: Expr[T2])(f: (T1, T2) => Option[R])(using GameObjectRepository): Option[R] =
        for (x <- expr1.get; y <- expr2.get; r <- f(x, y)) yield r


    extension[T, R] (using GameObjectRepository)(expr: Expr[T])
        private[expression] def ~>(f: T => Option[R]): Option[R] = Expr.resolve1(expr)(f)

    extension[T1, T2, R] (using GameObjectRepository)(pair: (Expr[T1], Expr[T2]))
        private[expression] def ~>(f: (T1, T2) => Option[R]): Option[R] = Expr.resolve2(pair._1, pair._2)(f)


    def apply(value: Boolean): BooleanExpr = BooleanExpr.Constant(value)

    def apply(value: Int): IntegerExpr = IntegerExpr.Constant(value)

    def apply(value: Double): DecimalExpr = DecimalExpr.Constant(value)

    def apply(value: String): StringExpr = StringExpr.Constant(value)

    def apply(value: Timestamp): TimestampExpr = TimestampExpr.Constant(value)

    def apply(value: Coordinates): CoordinatesExpr = CoordinatesExpr.Constant(value)

    def apply(value: Shift): ShiftExpr = ShiftExpr.Constant(value)

    def apply(value: Direction): DirectionExpr = DirectionExpr.Constant(value)


    extension[T: Ordering] (expr1: Expr[T])
        @targetName("less")
        def <(expr2: Expr[T]): BooleanExpr = BooleanExpr.Less(expr1, expr2)

        @targetName("lessEq")
        def <=(expr2: Expr[T]): BooleanExpr = BooleanExpr.LessEquals(expr1, expr2)

        @targetName("greater")
        def >(expr2: Expr[T]): BooleanExpr = BooleanExpr.Greater(expr1, expr2)

        @targetName("greaterEq")
        def >=(expr2: Expr[T]): BooleanExpr = BooleanExpr.GreaterEquals(expr1, expr2)

}
