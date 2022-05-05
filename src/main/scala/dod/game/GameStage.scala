package dod.game

import dod.game.event.Event
import dod.game.expression.ExprContext
import dod.game.gameobject.GameObjectRepository
import dod.game.model.Timer

import scala.collection.immutable.Queue
import scala.util.chaining.scalaUtilChainingOps

class GameStage(val gameObjects: GameObjectRepository) extends ExprContext {

    def updateGameObjects(gameObjects: GameObjectRepository): GameStage =
        GameStage(gameObjects = gameObjects)

}
