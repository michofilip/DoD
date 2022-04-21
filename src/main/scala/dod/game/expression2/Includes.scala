package dod.game.expression2

import dod.game.expression2.CoordinatesExpr.MoveBy
import dod.game.model.Timestamps.Timestamp
import dod.game.model.{Coordinates, Shift}

import scala.annotation.targetName

object Includes {

    extension[T] (expr1: Expr2[T])
        @targetName("equive")
        def ===(expr2: Expr2[T]): Expr2[Boolean] = BooleanExpr.Equals(expr1, expr2)

        @targetName("unequive")
        def !==(expr2: Expr2[T]): Expr2[Boolean] = BooleanExpr.Unequals(expr1, expr2)


    extension[T: Ordering] (expr1: Expr2[T])
        @targetName("less")
        def <(expr2: Expr2[T]): Expr2[Boolean] = BooleanExpr.Less(expr1, expr2)

        @targetName("lessEq")
        def <=(expr2: Expr2[T]): Expr2[Boolean] = BooleanExpr.LessEquals(expr1, expr2)

        @targetName("greater")
        def >(expr2: Expr2[T]): Expr2[Boolean] = BooleanExpr.Greater(expr1, expr2)

        @targetName("greaterEq")
        def >=(expr2: Expr2[T]): Expr2[Boolean] = BooleanExpr.GreaterEquals(expr1, expr2)
        

    extension (expr1: Expr2[Boolean])
        @targetName("not")
        def unary_! : Expr2[Boolean] = BooleanExpr.Not(expr1)

        @targetName("and")
        def &&(expr2: Expr2[Boolean]): Expr2[Boolean] = BooleanExpr.And(expr1, expr2)

        @targetName("or")
        def ||(expr2: Expr2[Boolean]): Expr2[Boolean] = BooleanExpr.Or(expr1, expr2)


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


    extension (expr1: Expr2[Double])
        @targetName("posDec")
        def unary_+ : Expr2[Double] = expr1

        @targetName("negDec")
        def unary_- : Expr2[Double] = DecimalExpr.Negation(expr1)

        @targetName("addDecInt")
        def +(expr2: Expr2[Int]): Expr2[Double] = DecimalExpr.Addition(expr1, expr2.toDecimalExpr)

        @targetName("addDecDec")
        def +(expr2: Expr2[Double]): Expr2[Double] = DecimalExpr.Addition(expr1, expr2)

        @targetName("subDecInt")
        def -(expr2: Expr2[Int]): Expr2[Double] = DecimalExpr.Subtraction(expr1, expr2.toDecimalExpr)

        @targetName("subDecDec")
        def -(expr2: Expr2[Double]): Expr2[Double] = DecimalExpr.Subtraction(expr1, expr2)

        @targetName("mulDecInt")
        def *(expr2: Expr2[Int]): Expr2[Double] = DecimalExpr.Multiplication(expr1, expr2.toDecimalExpr)

        @targetName("mulDecDec")
        def *(expr2: Expr2[Double]): Expr2[Double] = DecimalExpr.Multiplication(expr1, expr2)

        @targetName("divDecInt")
        def /(expr2: Expr2[Int]): Expr2[Double] = DecimalExpr.Division(expr1, expr2.toDecimalExpr)

        @targetName("divDecDec")
        def /(expr2: Expr2[Double]): Expr2[Double] = DecimalExpr.Division(expr1, expr2)

        def toIntegerExpr: Expr2[Int] = IntegerExpr.DecimalToInteger(expr1)

        @targetName("decToStringExpr")
        def toStringExpr: Expr2[String] = StringExpr.DecimalToString(expr1)


    extension (expr1: Expr2[String])
        @targetName("concat")
        def +(expr2: Expr2[String]): Expr2[String] = StringExpr.Concatenate(expr1, expr2)


    extension (expr1: Expr2[Coordinates])
        def moveBy(expr2: Expr2[Shift]): Expr2[Coordinates] = MoveBy(expr1, expr2)


    given Ordering[Timestamp] with {
        override def compare(x: Timestamp, y: Timestamp): Int = java.lang.Long.compare(x.value, y.value)
    }
}
