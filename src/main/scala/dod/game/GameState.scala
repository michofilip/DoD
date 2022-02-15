package dod.game

import dod.game.event.{Event, EventProcessor}
import dod.game.gameobject.GameObjectRepository
import dod.game.temporal.Timer

import scala.collection.immutable.Queue
import scala.util.chaining.scalaUtilChainingOps

class GameState(val gameObjectRepository: GameObjectRepository, val events: Queue[Event]) {
    def processEvent(using eventProcessor: EventProcessor): GameState = {
        events match {
            case event +: rest => eventProcessor.processEvent(event, gameObjectRepository).pipe {
                case (gameObjects, events) => new GameState(gameObjects, rest ++ events)
            }

            case _ => this
        }
    }

    override def toString = s"GameState($gameObjectRepository, $events)"
}
