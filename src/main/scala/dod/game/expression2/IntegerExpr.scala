package dod.game.expression2

import scala.annotation.targetName

object IntegerExpr {

    final case class Constant(value: Int) extends Expr2[Int] :
        override def resolve: Option[Int] = Some(value)

    final case class Negation(expr: Expr2[Int]) extends Expr2[Int] :
        override def resolve: Option[Int] = expr ~> (x => Some(-x))

    final case class Addition(expr1: Expr2[Int], expr2: Expr2[Int]) extends Expr2[Int] :
        override def resolve: Option[Int] = (expr1, expr2) ~> ((x, y) => Some(x + y))

    final case class Subtraction(expr1: Expr2[Int], expr2: Expr2[Int]) extends Expr2[Int] :
        override def resolve: Option[Int] = (expr1, expr2) ~> ((x, y) => Some(x - y))

    final case class Multiplication(expr1: Expr2[Int], expr2: Expr2[Int]) extends Expr2[Int] :
        override def resolve: Option[Int] = (expr1, expr2) ~> ((x, y) => Some(x * y))

    final case class Division(expr1: Expr2[Int], expr2: Expr2[Int]) extends Expr2[Int] :
        override def resolve: Option[Int] = (expr1, expr2) ~> ((x, y) => if y != 0 then Some(x / y) else None)

    final case class Reminder(expr1: Expr2[Int], expr2: Expr2[Int]) extends Expr2[Int] :
        override def resolve: Option[Int] = (expr1, expr2) ~> ((x, y) => if y != 0 then Some(x % y) else None)

    final case class DecimalToInteger(expr: Expr2[Double]) extends Expr2[Int] :
        override def resolve: Option[Int] = expr ~> (x => Some(x.toInt))

}
