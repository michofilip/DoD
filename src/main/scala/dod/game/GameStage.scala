package dod.game

import dod.game.event.Event
import dod.game.gameobject.GameObjectRepository
import dod.game.model.Timer

import scala.collection.immutable.Queue
import scala.util.chaining.scalaUtilChainingOps

class GameStage(val gameObjectRepository: GameObjectRepository
//                , val events: Queue[Event]
               ) {

    def updateGameObjectRepository(gameObjectRepository: GameObjectRepository): GameStage =
        GameStage(gameObjectRepository = gameObjectRepository
//            ,            events = events
        )


//    def addEvents(events: Seq[Event]): GameStage =
//        GameStage(gameObjectRepository = gameObjectRepository, events = this.events ++ events)
//
//
//    def clearEvents(): GameStage =
//        GameStage(gameObjectRepository = gameObjectRepository, events = Queue.empty)

}
