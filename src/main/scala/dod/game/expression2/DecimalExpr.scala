package dod.game.expression2

import dod.game.expression2.IntegerExpr.*

import scala.annotation.targetName

object DecimalExpr {

    extension (expr1: Expr2[Double])
        @targetName("pos")
        def unary_+ : Expr2[Double] = expr1

        @targetName("neg")
        def unary_- : Expr2[Double] = DecimalExpr.Negation(expr1)

        @targetName("addInt")
        def +(expr2: Expr2[Int]): Expr2[Double] = DecimalExpr.Addition(expr1, expr2.toDecimalExpr)

        @targetName("addDec")
        def +(expr2: Expr2[Double]): Expr2[Double] = DecimalExpr.Addition(expr1, expr2)

        @targetName("subInt")
        def -(expr2: Expr2[Int]): Expr2[Double] = DecimalExpr.Subtraction(expr1, expr2.toDecimalExpr)

        @targetName("subDec")
        def -(expr2: Expr2[Double]): Expr2[Double] = DecimalExpr.Subtraction(expr1, expr2)

        @targetName("mulInt")
        def *(expr2: Expr2[Int]): Expr2[Double] = DecimalExpr.Multiplication(expr1, expr2.toDecimalExpr)

        @targetName("mulDec")
        def *(expr2: Expr2[Double]): Expr2[Double] = DecimalExpr.Multiplication(expr1, expr2)

        @targetName("divInt")
        def /(expr2: Expr2[Int]): Expr2[Double] = DecimalExpr.Division(expr1, expr2.toDecimalExpr)

        @targetName("divDec")
        def /(expr2: Expr2[Double]): Expr2[Double] = DecimalExpr.Division(expr1, expr2)

        def toIntegerExpr: Expr2[Int] = IntegerExpr.DecimalToInteger(expr1)

        def toStringExpr: Expr2[String] = StringExpr.DecimalToString(expr1)


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
