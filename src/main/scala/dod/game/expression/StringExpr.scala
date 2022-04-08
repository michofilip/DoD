package dod.game.expression

import dod.game.expression.StringExpr.Concatenate

abstract class StringExpr extends Expr[String] {

    def +(that: StringExpr): StringExpr = Concatenate(this, that)

}

object StringExpr {

    final case class Constant(value: String) extends StringExpr

    final case class Concatenate(expr1: StringExpr, expr2: StringExpr) extends StringExpr

    final case class IntegerToString(expr: IntegerExpr) extends StringExpr

    final case class DecimalToString(expr: DecimalExpr) extends StringExpr

}
