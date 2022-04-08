package dod.game.expression

import dod.game.expression.BooleanExpr.{And, Not, Or}

abstract class BooleanExpr extends Expr[Boolean] {

    def unary_! : BooleanExpr = Not(this)

    def &&(that: BooleanExpr): BooleanExpr = And(this, that)

    def ||(that: BooleanExpr): BooleanExpr = Or(this, that)

}

object BooleanExpr {

    final case class Constant(value: Boolean) extends BooleanExpr

    final case class Not(expr: BooleanExpr) extends BooleanExpr

    final case class And(expr1: BooleanExpr, expr2: BooleanExpr) extends BooleanExpr

    final case class Or(expr1: BooleanExpr, expr2: BooleanExpr) extends BooleanExpr

    final case class Equals[T](expr1: Expr[T], expr2: Expr[T]) extends BooleanExpr

    final case class UnEquals[T](expr1: Expr[T], expr2: Expr[T]) extends BooleanExpr

    final case class Less[T](expr1: OrderedExpr[T], expr2: OrderedExpr[T]) extends BooleanExpr

    final case class LessEquals[T](expr1: OrderedExpr[T], expr2: OrderedExpr[T]) extends BooleanExpr

    final case class Greater[T](expr1: OrderedExpr[T], expr2: OrderedExpr[T]) extends BooleanExpr

    final case class GreaterEquals[T](expr1: OrderedExpr[T], expr2: OrderedExpr[T]) extends BooleanExpr

}
