package dod.game.expression

import dod.game.expression.IntegerExpr.*

abstract class IntegerExpr extends OrderedExpr[Int] {

    override def compare(x: Int, y: Int): Int = java.lang.Integer.compare(x, y)

    def unary_+ : IntegerExpr = this

    def unary_- : IntegerExpr = Negation(this)

    def +(that: IntegerExpr): IntegerExpr = Addition(this, that)

    def +(that: DecimalExpr): DecimalExpr = DecimalExpr.Addition(this.toDecimalExpr, that)

    def -(that: IntegerExpr): IntegerExpr = Subtraction(this, that)

    def -(that: DecimalExpr): DecimalExpr = DecimalExpr.Subtraction(this.toDecimalExpr, that)

    def *(that: IntegerExpr): IntegerExpr = Multiplication(this, that)

    def *(that: DecimalExpr): DecimalExpr = DecimalExpr.Multiplication(this.toDecimalExpr, that)

    def /(that: IntegerExpr): IntegerExpr = Division(this, that)

    def /(that: DecimalExpr): DecimalExpr = DecimalExpr.Division(this.toDecimalExpr, that)

    def %(that: IntegerExpr): IntegerExpr = Reminder(this, that)

    def toIntegerExpr: IntegerExpr = this

    def toDecimalExpr: DecimalExpr = DecimalExpr.IntegerToDecimal(this)

    def toStringExpr: StringExpr = StringExpr.IntegerToString(this)

}

object IntegerExpr {

    final case class Constant(value: Int) extends IntegerExpr

    final case class Negation(expr: IntegerExpr) extends IntegerExpr

    final case class Addition(expr1: IntegerExpr, expr2: IntegerExpr) extends IntegerExpr

    final case class Subtraction(expr1: IntegerExpr, expr2: IntegerExpr) extends IntegerExpr

    final case class Multiplication(expr1: IntegerExpr, expr2: IntegerExpr) extends IntegerExpr

    final case class Division(expr1: IntegerExpr, expr2: IntegerExpr) extends IntegerExpr

    final case class Reminder(expr1: IntegerExpr, expr2: IntegerExpr) extends IntegerExpr

    final case class DecimalToInteger(expr: DecimalExpr) extends IntegerExpr

}
