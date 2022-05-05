package dod.game

import dod.game.event.Event
import dod.game.expression.Expr.ExprContext
import dod.game.gameobject.GameObjectRepository
import dod.game.model.Timer

import scala.collection.immutable.Queue
import scala.util.chaining.scalaUtilChainingOps

class GameStage(val gameObjectRepository: GameObjectRepository) extends ExprContext {

    def updateGameObjectRepository(gameObjectRepository: GameObjectRepository): GameStage =
        GameStage(gameObjectRepository = gameObjectRepository)

}
