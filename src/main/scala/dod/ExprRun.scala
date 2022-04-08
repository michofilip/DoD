package dod

import dod.game.expression.{BooleanExpr, Expr, GameObjectExpr}
import dod.game.gameobject.GameObjectRepository
import dod.game.model.Coordinates
import dod.game.model.Timestamps.Timestamp
import dod.service.ExpressionService

import java.util.UUID

object ExprRun {

    @main
    def run(): Unit = {
        val context = new Context

        val id = UUID.randomUUID
        val gameObject = context.gameObjectService.createWall(id, Timestamp.zero, Coordinates(0, 0))

        given GameObjectRepository = GameObjectRepository() + gameObject

        val v1: Expr[Boolean] = Expr(true) || (Expr(false) && !Expr(true))
        val v2: Expr[Boolean] = (Expr(2) + Expr(1)) < Expr(3)
        val v3 = GameObjectExpr.GetName(id)

        println(v1)
        println(v2)
        println(v3)

        val valueService = new ExpressionService

        println(valueService.resolve(v1))
        println(valueService.resolve(v2))
        println(valueService.resolve(v3))

    }
}
