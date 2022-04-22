package dod.game.expression

import dod.game.expression.DecimalExpr.{Addition, Division, Multiplication, Negation, Subtraction}
import dod.game.expression.Expr.ExprContext
import dod.game.gameobject.GameObjectRepository

abstract class DecimalExpr extends OrderedExpr[Double] {

    final def unary_+ : DecimalExpr = this

    final def unary_- : DecimalExpr = Negation(this)

    final def +(that: IntegerExpr): DecimalExpr = Addition(this, that.toDecimalExpr)

    final def +(that: DecimalExpr): DecimalExpr = Addition(this, that)

    final def -(that: IntegerExpr): DecimalExpr = Subtraction(this, that.toDecimalExpr)

    final def -(that: DecimalExpr): DecimalExpr = Subtraction(this, that)

    final def *(that: IntegerExpr): DecimalExpr = Multiplication(this, that.toDecimalExpr)

    final def *(that: DecimalExpr): DecimalExpr = Multiplication(this, that)

    final def /(that: IntegerExpr): DecimalExpr = Division(this, that.toDecimalExpr)

    final def /(that: DecimalExpr): DecimalExpr = Division(this, that)

    final def toIntegerExpr: IntegerExpr = IntegerExpr.DecimalToInteger(this)

    final def toDecimalExpr: DecimalExpr = this

    final def toStringExpr: StringExpr = StringExpr.DecimalToString(this)

}

object DecimalExpr {

    final case class Constant(value: Double) extends DecimalExpr :
        override def get(using ExprContext): Option[Double] = Some(value)

    final case class Negation(expr: DecimalExpr) extends DecimalExpr :
        override def get(using ExprContext): Option[Double] = expr ~> (x => Some(-x))

    final case class Addition(expr1: DecimalExpr, expr2: DecimalExpr) extends DecimalExpr :
        override def get(using ExprContext): Option[Double] = (expr1, expr2) ~> ((x, y) => Some(x + y))

    final case class Subtraction(expr1: DecimalExpr, expr2: DecimalExpr) extends DecimalExpr :
        override def get(using ExprContext): Option[Double] = (expr1, expr2) ~> ((x, y) => Some(x - y))

    final case class Multiplication(expr1: DecimalExpr, expr2: DecimalExpr) extends DecimalExpr :
        override def get(using ExprContext): Option[Double] = (expr1, expr2) ~> ((x, y) => Some(x * y))

    final case class Division(expr1: DecimalExpr, expr2: DecimalExpr) extends DecimalExpr :
        override def get(using ExprContext): Option[Double] = (expr1, expr2) ~> ((x, y) => if (y != 0) Some(x / y) else None)

    final case class IntegerToDecimal(expr: IntegerExpr) extends DecimalExpr :
        override def get(using ExprContext): Option[Double] = expr ~> (x => Some(x.toDouble))

}
