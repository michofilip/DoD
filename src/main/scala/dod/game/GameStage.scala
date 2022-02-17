package dod.game

import dod.game.event.{Event, EventProcessor}
import dod.game.gameobject.GameObjectRepository
import dod.game.temporal.Timer

import scala.collection.immutable.Queue
import scala.util.chaining.scalaUtilChainingOps

class GameStage(val gameObjectRepository: GameObjectRepository, val events: Queue[Event]) {

    def updateGameObjectRepository(gameObjectRepository: GameObjectRepository): GameStage =
        GameStage(gameObjectRepository = gameObjectRepository, events = events)


    def addEvents(events: Seq[Event]): GameStage =
        GameStage(gameObjectRepository = gameObjectRepository, events = this.events ++ events)


    def clearEvents(): GameStage =
        GameStage(gameObjectRepository = gameObjectRepository, events = Queue.empty)


    override def toString = s"GameState(gameObjects=$gameObjectRepository,events=${events.mkString("[", ",", "]")})"
}
