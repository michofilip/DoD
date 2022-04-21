package dod.game.expression2

import dod.game.expression2.IntegerExpr.*

import scala.annotation.targetName

object DecimalExpr {

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
