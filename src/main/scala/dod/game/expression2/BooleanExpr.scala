package dod.game.expression2

import scala.annotation.targetName

object BooleanExpr {

    extension (expr: Expr2[Boolean])
        @targetName("not")
        def unary_! : Expr2[Boolean] = BooleanExpr.Not(expr)

        @targetName("and")
        def &&(expr2: Expr2[Boolean]): Expr2[Boolean] = BooleanExpr.And(expr, expr2)

        @targetName("or")
        def ||(expr2: Expr2[Boolean]): Expr2[Boolean] = BooleanExpr.Or(expr, expr2)


    final case class Constant(value: Boolean) extends Expr2[Boolean] :
        override def resolve: Option[Boolean] = Some(value)

    final case class Not(expr: Expr2[Boolean]) extends Expr2[Boolean] :
        override def resolve: Option[Boolean] = expr ~> (x => Some(!x))

    final case class And(expr1: Expr2[Boolean], expr2: Expr2[Boolean]) extends Expr2[Boolean] :
        override def resolve: Option[Boolean] = (expr1, expr2) ~> ((x, y) => Some(x && y))

    final case class Or(expr1: Expr2[Boolean], expr2: Expr2[Boolean]) extends Expr2[Boolean] :
        override def resolve: Option[Boolean] = (expr1, expr2) ~> ((x, y) => Some(x || y))

    final case class Equals[T](expr1: Expr2[T], expr2: Expr2[T]) extends Expr2[Boolean] :
        override def resolve: Option[Boolean] = (expr1, expr2) ~> ((x, y) => Some(x == y))

    final case class Unequals[T](expr1: Expr2[T], expr2: Expr2[T]) extends Expr2[Boolean] :
        override def resolve: Option[Boolean] = (expr1, expr2) ~> ((x, y) => Some(x != y))

    final case class Less[T: Ordering](expr1: Expr2[T], expr2: Expr2[T]) extends Expr2[Boolean] :
        override def resolve: Option[Boolean] = (expr1, expr2) ~> ((x, y) => Some(summon[Ordering[T]].compare(x, y) < 0))

    final case class LessEquals[T: Ordering](expr1: Expr2[T], expr2: Expr2[T]) extends Expr2[Boolean] :
        override def resolve: Option[Boolean] = (expr1, expr2) ~> ((x, y) => Some(summon[Ordering[T]].compare(x, y) <= 0))

    final case class Greater[T: Ordering](expr1: Expr2[T], expr2: Expr2[T]) extends Expr2[Boolean] :
        override def resolve: Option[Boolean] = (expr1, expr2) ~> ((x, y) => Some(summon[Ordering[T]].compare(x, y) > 0))

    final case class GreaterEquals[T: Ordering](expr1: Expr2[T], expr2: Expr2[T]) extends Expr2[Boolean] :
        override def resolve: Option[Boolean] = (expr1, expr2) ~> ((x, y) => Some(summon[Ordering[T]].compare(x, y) >= 0))

}
