package dod.game.expression2

import dod.game.expression2.IntegerExpr.*

import scala.annotation.targetName

object DecimalExpr {

    extension (expr: Expr2[Double])
        @targetName("pos_dec")
        def unary_+ : Expr2[Double] = expr

        @targetName("neg_dec")
        def unary_- : Expr2[Double] = DecimalExpr.Negation(expr)

        @targetName("add_dec_int")
        def +(expr2: Expr2[Int]): Expr2[Double] = DecimalExpr.Addition(expr, expr2.toDecimalExpr)

        @targetName("add_dec_dec")
        def +(expr2: Expr2[Double]): Expr2[Double] = DecimalExpr.Addition(expr, expr2)

        @targetName("sub_dec_int")
        def -(expr2: Expr2[Int]): Expr2[Double] = DecimalExpr.Subtraction(expr, expr2.toDecimalExpr)

        @targetName("sub_dec_dec")
        def -(expr2: Expr2[Double]): Expr2[Double] = DecimalExpr.Subtraction(expr, expr2)

        @targetName("mul_dec_int")
        def *(expr2: Expr2[Int]): Expr2[Double] = DecimalExpr.Multiplication(expr, expr2.toDecimalExpr)

        @targetName("mul_dec_dec")
        def *(expr2: Expr2[Double]): Expr2[Double] = DecimalExpr.Multiplication(expr, expr2)

        @targetName("div_dec_int")
        def /(expr2: Expr2[Int]): Expr2[Double] = DecimalExpr.Division(expr, expr2.toDecimalExpr)

        @targetName("div_dec_dec")
        def /(expr2: Expr2[Double]): Expr2[Double] = DecimalExpr.Division(expr, expr2)

        def toIntegerExpr: Expr2[Int] = IntegerExpr.DecimalToInteger(expr)

        def toStringExpr: Expr2[String] = StringExpr.DecimalToString(expr)


    final case class Constant(value: Double) extends Expr2[Double] :
        override def resolve: Option[Double] = Some(value)

    final case class Negation(expr: Expr2[Double]) extends Expr2[Double] :
        override def resolve: Option[Double] = expr ~> (x => Some(-x))

    final case class Addition(expr1: Expr2[Double], expr2: Expr2[Double]) extends Expr2[Double] :
        override def resolve: Option[Double] = (expr1, expr2) ~> ((x, y) => Some(x + y))

    final case class Subtraction(expr1: Expr2[Double], expr2: Expr2[Double]) extends Expr2[Double] :
        override def resolve: Option[Double] = (expr1, expr2) ~> ((x, y) => Some(x - y))

    final case class Multiplication(expr1: Expr2[Double], expr2: Expr2[Double]) extends Expr2[Double] :
        override def resolve: Option[Double] = (expr1, expr2) ~> ((x, y) => Some(x * y))

    final case class Division(expr1: Expr2[Double], expr2: Expr2[Double]) extends Expr2[Double] :
        override def resolve: Option[Double] = (expr1, expr2) ~> ((x, y) => if y != 0 then Some(x / y) else None)

    final case class IntegerToDecimal(expr: Expr2[Int]) extends Expr2[Double] :
        override def resolve: Option[Double] = expr ~> (x => Some(x.toDouble))

}
