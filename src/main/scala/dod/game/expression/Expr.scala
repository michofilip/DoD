package dod.game.expression

abstract class Expr[T] {

    def ===(that: Expr[T]): BooleanExpr = BooleanExpr.Equals(this, that)

    def !==(that: Expr[T]): BooleanExpr = BooleanExpr.UnEquals(this, that)

}

object Expr {

    def apply(value: Boolean): BooleanExpr = BooleanExpr.Constant(value)

    def apply(value: Int): IntegerExpr = IntegerExpr.Constant(value)

    def apply(value: Double): DecimalExpr = DecimalExpr.Constant(value)

}
