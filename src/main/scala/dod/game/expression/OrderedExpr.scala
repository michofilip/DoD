package dod.game.expression

trait OrderedExpr[T] extends Expr[T] with Ordering[T] {

    def <(that: OrderedExpr[T]): BooleanExpr = BooleanExpr.Less(this, that)

    def <=(that: OrderedExpr[T]): BooleanExpr = BooleanExpr.LessEquals(this, that)

    def >(that: OrderedExpr[T]): BooleanExpr = BooleanExpr.Greater(this, that)

    def >=(that: OrderedExpr[T]): BooleanExpr = BooleanExpr.GreaterEquals(this, that)

}
