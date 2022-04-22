package dod.game.expression

import scala.annotation.targetName

trait OrderedExpr[T: Ordering] extends Expr[T] {

    def <(that: OrderedExpr[T]): BooleanExpr = BooleanExpr.Less(this, that)

    def <=(that: OrderedExpr[T]): BooleanExpr = BooleanExpr.LessEquals(this, that)

    def >(that: OrderedExpr[T]): BooleanExpr = BooleanExpr.Greater(this, that)

    def >=(that: OrderedExpr[T]): BooleanExpr = BooleanExpr.GreaterEquals(this, that)

}
