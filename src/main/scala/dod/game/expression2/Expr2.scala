package dod.game.expression2

import scala.annotation.targetName

abstract class Expr2[T]:
    def resolve: Option[T]


object Expr2 {

    def apply(value: Boolean): Expr2[Boolean] = BooleanExpr.Constant(value)

    def apply(value: Int): Expr2[Int] = IntegerExpr.Constant(value)

    def apply(value: Double): Expr2[Double] = DecimalExpr.Constant(value)

    def apply(value: String): Expr2[String] = StringExpr.Constant(value)


    private[expression2] def resolve1[T, R](expr: Expr2[T])(f: T => Option[R]): Option[R] =
        for (x <- expr.resolve; r <- f(x)) yield r

    private[expression2] def resolve2[T1, T2, R](expr1: Expr2[T1], expr2: Expr2[T2])(f: (T1, T2) => Option[R]): Option[R] =
        for (x <- expr1.resolve; y <- expr2.resolve; r <- f(x, y)) yield r


    extension[T, R] (expr: Expr2[T])
        private[expression2] def ~>(f: T => Option[R]): Option[R] = Expr2.resolve1(expr)(f)

    extension[T1, T2, R] (pair: (Expr2[T1], Expr2[T2]))
        private[expression2] def ~>(f: (T1, T2) => Option[R]): Option[R] = Expr2.resolve2(pair._1, pair._2)(f)


    extension[T] (expr1: Expr2[T])
        @targetName("equive")
        def ===(expr2: Expr2[T]): Expr2[Boolean] = BooleanExpr.Equals(expr1, expr2)

        @targetName("unequive")
        def !==(expr2: Expr2[T]): Expr2[Boolean] = BooleanExpr.Unequals(expr1, expr2)


    extension[T: Ordering] (expr1: Expr2[T])
        @targetName("less")
        def <(expr2: Expr2[T]): Expr2[Boolean] = BooleanExpr.Less(expr1, expr2)

        @targetName("lessEq")
        def <=(expr2: Expr2[T]): Expr2[Boolean] = BooleanExpr.LessEquals(expr1, expr2)

        @targetName("greater")
        def >(expr2: Expr2[T]): Expr2[Boolean] = BooleanExpr.Greater(expr1, expr2)

        @targetName("greaterEq")
        def >=(expr2: Expr2[T]): Expr2[Boolean] = BooleanExpr.GreaterEquals(expr1, expr2)

}