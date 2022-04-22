package dod.game.expression

import dod.game.expression.Expr.ExprContext
import dod.game.expression.IntegerExpr.*
import dod.game.gameobject.GameObjectRepository

abstract class IntegerExpr extends Expr[Int] {

    final def unary_+ : IntegerExpr = this

    final def unary_- : IntegerExpr = Negation(this)

    final def +(that: IntegerExpr): IntegerExpr = Addition(this, that)

    final def +(that: DecimalExpr): DecimalExpr = DecimalExpr.Addition(this.toDecimalExpr, that)

    final def -(that: IntegerExpr): IntegerExpr = Subtraction(this, that)

    final def -(that: DecimalExpr): DecimalExpr = DecimalExpr.Subtraction(this.toDecimalExpr, that)

    final def *(that: IntegerExpr): IntegerExpr = Multiplication(this, that)

    final def *(that: DecimalExpr): DecimalExpr = DecimalExpr.Multiplication(this.toDecimalExpr, that)

    final def /(that: IntegerExpr): IntegerExpr = Division(this, that)

    final def /(that: DecimalExpr): DecimalExpr = DecimalExpr.Division(this.toDecimalExpr, that)

    final def %(that: IntegerExpr): IntegerExpr = Reminder(this, that)

    final def toIntegerExpr: IntegerExpr = this

    final def toDecimalExpr: DecimalExpr = DecimalExpr.IntegerToDecimal(this)

    final def toStringExpr: StringExpr = StringExpr.IntegerToString(this)

}

object IntegerExpr {

    final case class Constant(value: Int) extends IntegerExpr :
        override def get(using ExprContext): Option[Int] = Some(value)

    final case class Negation(expr: IntegerExpr) extends IntegerExpr :
        override def get(using ExprContext): Option[Int] = expr ~> (x => Some(-x))

    final case class Addition(expr1: IntegerExpr, expr2: IntegerExpr) extends IntegerExpr :
        override def get(using ExprContext): Option[Int] = (expr1, expr2) ~> ((x, y) => Some(x + y))

    final case class Subtraction(expr1: IntegerExpr, expr2: IntegerExpr) extends IntegerExpr :
        override def get(using ExprContext): Option[Int] = (expr1, expr2) ~> ((x, y) => Some(x - y))

    final case class Multiplication(expr1: IntegerExpr, expr2: IntegerExpr) extends IntegerExpr :
        override def get(using ExprContext): Option[Int] = (expr1, expr2) ~> ((x, y) => Some(x * y))

    final case class Division(expr1: IntegerExpr, expr2: IntegerExpr) extends IntegerExpr :
        override def get(using ExprContext): Option[Int] = (expr1, expr2) ~> ((x, y) => if (y != 0) Some(x / y) else None)

    final case class Reminder(expr1: IntegerExpr, expr2: IntegerExpr) extends IntegerExpr :
        override def get(using ExprContext): Option[Int] = (expr1, expr2) ~> ((x, y) => if (y != 0) Some(x % y) else None)

    final case class DecimalToInteger(expr: DecimalExpr) extends IntegerExpr :
        override def get(using ExprContext): Option[Int] = expr ~> (x => Some(x.toInt))

}
