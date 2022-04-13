package dod.game.expression

import dod.game.expression.DecimalExpr.{Addition, Division, Multiplication, Negation, Subtraction}

abstract class DecimalExpr extends OrderedExpr[Double] {

    override def compare(x: Double, y: Double): Int = java.lang.Double.compare(x, y)

    def unary_+ : DecimalExpr = this

    def unary_- : DecimalExpr = Negation(this)

    def +(that: IntegerExpr): DecimalExpr = Addition(this, that.toDecimalExpr)

    def +(that: DecimalExpr): DecimalExpr = Addition(this, that)

    def -(that: IntegerExpr): DecimalExpr = Subtraction(this, that.toDecimalExpr)

    def -(that: DecimalExpr): DecimalExpr = Subtraction(this, that)

    def *(that: IntegerExpr): DecimalExpr = Multiplication(this, that.toDecimalExpr)

    def *(that: DecimalExpr): DecimalExpr = Multiplication(this, that)

    def /(that: IntegerExpr): DecimalExpr = Division(this, that.toDecimalExpr)

    def /(that: DecimalExpr): DecimalExpr = Division(this, that)

    def toIntegerExpr: IntegerExpr = IntegerExpr.DecimalToInteger(this)

    def toDecimalExpr: DecimalExpr = this

    def toStringExpr: StringExpr = StringExpr.DecimalToString(this)

}

object DecimalExpr {

    final case class Constant(value: Double) extends DecimalExpr

    final case class Negation(expr: DecimalExpr) extends DecimalExpr

    final case class Addition(expr1: DecimalExpr, expr2: DecimalExpr) extends DecimalExpr

    final case class Subtraction(expr1: DecimalExpr, expr2: DecimalExpr) extends DecimalExpr

    final case class Multiplication(expr1: DecimalExpr, expr2: DecimalExpr) extends DecimalExpr

    final case class Division(expr1: DecimalExpr, expr2: DecimalExpr) extends DecimalExpr

    final case class IntegerToDecimal(expr: IntegerExpr) extends DecimalExpr

}
