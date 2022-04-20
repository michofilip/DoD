package dod.game.expression2

import scala.annotation.targetName

object IntegerExpr {

    extension (expr: Expr2[Int])
        @targetName("pos_int")
        def unary_+ : Expr2[Int] = expr

        @targetName("neg_int")
        def unary_- : Expr2[Int] = IntegerExpr.Negation(expr)

        @targetName("add_int_int")
        def +(expr2: Expr2[Int]): Expr2[Int] = IntegerExpr.Addition(expr, expr2)

        @targetName("add_int_dec")
        def +(expr2: Expr2[Double]): Expr2[Double] = DecimalExpr.Addition(expr.toDecimalExpr, expr2)

        @targetName("sub_int_int")
        def -(expr2: Expr2[Int]): Expr2[Int] = IntegerExpr.Subtraction(expr, expr2)

        @targetName("sub_int_dec")
        def -(expr2: Expr2[Double]): Expr2[Double] = DecimalExpr.Subtraction(expr.toDecimalExpr, expr2)

        @targetName("mul_int_int")
        def *(expr2: Expr2[Int]): Expr2[Int] = IntegerExpr.Multiplication(expr, expr2)

        @targetName("mul_int_dec")
        def *(expr2: Expr2[Double]): Expr2[Double] = DecimalExpr.Multiplication(expr.toDecimalExpr, expr2)

        @targetName("div_int_int")
        def /(expr2: Expr2[Int]): Expr2[Int] = IntegerExpr.Division(expr, expr2)

        @targetName("div_int_dec")
        def /(expr2: Expr2[Double]): Expr2[Double] = DecimalExpr.Division(expr.toDecimalExpr, expr2)

        @targetName("rem")
        def %(expr2: Expr2[Int]): Expr2[Int] = IntegerExpr.Reminder(expr, expr2)

        def toDecimalExpr: Expr2[Double] = DecimalExpr.IntegerToDecimal(expr)

        def toStringExpr: Expr2[String] = StringExpr.IntegerToString(expr)


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
