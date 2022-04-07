package dod

import dod.game.expression.{BooleanExpr, Expr}
import dod.service.ExpressionService

object ExprRun {

    @main
    def run(): Unit = {
        val v1: Expr[Boolean] = Expr(true) || (Expr(false) && !Expr(true))
        val v2: Expr[Boolean] = (Expr(2) + Expr(1)) < Expr(3)

        println(v1)
        println(v2)

        val valueService = new ExpressionService

        println(valueService.resolve(v1))
        println(valueService.resolve(v2))

    }
}
