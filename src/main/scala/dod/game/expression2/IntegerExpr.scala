package dod.game.expression2

import scala.annotation.targetName

object IntegerExpr {

    extension (expr1: Expr2[Int])
        @targetName("pos")
        def unary_+ : Expr2[Int] = expr1

        @targetName("neg")
        def unary_- : Expr2[Int] = IntegerExpr.Negation(expr1)

        @targetName("addInt")
        def +(expr2: Expr2[Int]): Expr2[Int] = IntegerExpr.Addition(expr1, expr2)

        @targetName("addDec")
        def +(expr2: Expr2[Double]): Expr2[Double] = DecimalExpr.Addition(expr1.toDecimalExpr, expr2)

        @targetName("subInt")
        def -(expr2: Expr2[Int]): Expr2[Int] = IntegerExpr.Subtraction(expr1, expr2)

        @targetName("subDec")
        def -(expr2: Expr2[Double]): Expr2[Double] = DecimalExpr.Subtraction(expr1.toDecimalExpr, expr2)

        @targetName("mulInt")
        def *(expr2: Expr2[Int]): Expr2[Int] = IntegerExpr.Multiplication(expr1, expr2)

        @targetName("mulDec")
        def *(expr2: Expr2[Double]): Expr2[Double] = DecimalExpr.Multiplication(expr1.toDecimalExpr, expr2)

        @targetName("divInt")
        def /(expr2: Expr2[Int]): Expr2[Int] = IntegerExpr.Division(expr1, expr2)

        @targetName("divDec")
        def /(expr2: Expr2[Double]): Expr2[Double] = DecimalExpr.Division(expr1.toDecimalExpr, expr2)

        @targetName("rem")
        def %(expr2: Expr2[Int]): Expr2[Int] = IntegerExpr.Reminder(expr1, expr2)

        def toDecimalExpr: Expr2[Double] = DecimalExpr.IntegerToDecimal(expr1)

        def toStringExpr: Expr2[String] = StringExpr.IntegerToString(expr1)


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
