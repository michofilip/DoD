package dod.game.expression

import dod.game.expression.BooleanExpr.{And, Not, Or}
import dod.game.expression.Expr.*
import dod.game.gameobject.GameObjectRepository

abstract class BooleanExpr extends Expr[Boolean] {

    final def unary_! : BooleanExpr = Not(this)

    final def &&(that: BooleanExpr): BooleanExpr = And(this, that)

    final def ||(that: BooleanExpr): BooleanExpr = Or(this, that)

}

object BooleanExpr {

    final case class Constant(value: Boolean) extends BooleanExpr :
        override def get(using ExprContext): Option[Boolean] = Some(value)

    final case class Not(expr: BooleanExpr) extends BooleanExpr :
        override def get(using ExprContext): Option[Boolean] = expr ~> (x => Some(!x))

    final case class And(expr1: BooleanExpr, expr2: BooleanExpr) extends BooleanExpr :
        override def get(using ExprContext): Option[Boolean] = (expr1, expr2) ~> ((x, y) => Some(x && y))

    final case class Or(expr1: BooleanExpr, expr2: BooleanExpr) extends BooleanExpr :
        override def get(using ExprContext): Option[Boolean] = (expr1, expr2) ~> ((x, y) => Some(x || y))

    final case class Equals[T](expr1: Expr[T], expr2: Expr[T]) extends BooleanExpr :
        override def get(using ExprContext): Option[Boolean] = (expr1, expr2) ~> ((x, y) => Some(x == y))

    final case class UnEquals[T](expr1: Expr[T], expr2: Expr[T]) extends BooleanExpr :
        override def get(using ExprContext): Option[Boolean] = (expr1, expr2) ~> ((x, y) => Some(x != y))

    final case class Less[T: Ordering](expr1: OrderedExpr[T], expr2: OrderedExpr[T]) extends BooleanExpr :
        override def get(using ExprContext): Option[Boolean] = (expr1, expr2) ~> ((x, y) => Some(summon[Ordering[T]].compare(x, y) < 0))

    final case class LessEquals[T: Ordering](expr1: OrderedExpr[T], expr2: OrderedExpr[T]) extends BooleanExpr :
        override def get(using ExprContext): Option[Boolean] = (expr1, expr2) ~> ((x, y) => Some(summon[Ordering[T]].compare(x, y) <= 0))

    final case class Greater[T: Ordering](expr1: OrderedExpr[T], expr2: OrderedExpr[T]) extends BooleanExpr :
        override def get(using ExprContext): Option[Boolean] = (expr1, expr2) ~> ((x, y) => Some(summon[Ordering[T]].compare(x, y) > 0))

    final case class GreaterEquals[T: Ordering](expr1: OrderedExpr[T], expr2: OrderedExpr[T]) extends BooleanExpr :
        override def get(using ExprContext): Option[Boolean] = (expr1, expr2) ~> ((x, y) => Some(summon[Ordering[T]].compare(x, y) >= 0))

}
