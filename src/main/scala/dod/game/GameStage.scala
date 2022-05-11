package dod.game

import dod.game.event.Event
import dod.game.expression.ExprContext
import dod.game.gameobject.GameObjectRepository
import dod.game.model.Timestamps.Timestamp
import dod.game.model.{Coordinates, Timer}

import scala.collection.immutable.Queue
import scala.util.chaining.scalaUtilChainingOps

case class GameStage(gameObjects: GameObjectRepository, focusId: Option[String] = None) extends ExprContext {

    def updateGameObjects(gameObjects: GameObjectRepository): GameStage =
        copy(gameObjects = gameObjects)

    def updateGameObjects(mapping: GameObjectRepository => GameObjectRepository): GameStage =
        updateGameObjects(mapping(gameObjects))

    def focus: Coordinates = focusId
        .flatMap(gameObjects.findById)
        .flatMap(_.position.coordinates)
        .getOrElse(Coordinates(0, 0))

    def timestamp: Timestamp = gameObjects
        .findTimer("global_timers", "timer_1")
        .fold(Timestamp.zero)(_.timestamp)

}
