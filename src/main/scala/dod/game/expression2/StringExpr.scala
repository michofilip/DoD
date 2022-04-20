package dod.game.expression2

import dod.game.expression.{DecimalExpr, IntegerExpr, StringExpr}

import scala.annotation.targetName

object StringExpr {

    extension (expr: Expr2[String])
        @targetName("concat")
        def +(expr2: Expr2[String]): Expr2[String] = StringExpr.Concatenate(expr, expr2)


    final case class Constant(value: String) extends Expr2[String] :
        override def resolve: Option[String] = Some(value)

    final case class Concatenate(expr1: Expr2[String], expr2: Expr2[String]) extends Expr2[String] :
        override def resolve: Option[String] = (expr1, expr2) ~> ((x, y) => Some(x + y))

    final case class IntegerToString(expr: Expr2[Int]) extends Expr2[String] :
        override def resolve: Option[String] = expr ~> (x => Some(x.toString))

    final case class DecimalToString(expr: Expr2[Double]) extends Expr2[String] :
        override def resolve: Option[String] = expr ~> (x => Some(x.toString))

}
