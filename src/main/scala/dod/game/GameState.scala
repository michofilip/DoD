package dod.game

import dod.game.event.{Event, EventProcessor}
import dod.game.gameobject.GameObjectRepository
import dod.game.temporal.Timer

import scala.collection.immutable.Queue
import scala.util.chaining.scalaUtilChainingOps

class GameState(val gameObjectRepository: GameObjectRepository, val events: Queue[Event]) {
    def processEvent(using eventProcessor: EventProcessor): GameState = {
        events match
            case event +: rest => eventProcessor.processEvent(gameObjectRepository, event).pipe {
                case (gameObjects, events) => GameState(gameObjects, rest ++ events)
            }

            case _ => this
    }

    def processEvents(using eventProcessor: EventProcessor): GameState = {
        eventProcessor.processEvents(gameObjectRepository, events).pipe {
            case (gameObjects, events) => GameState(gameObjects, Queue(events: _*))
        }
    }

    override def toString = s"GameState(gameObjects=$gameObjectRepository,events=${events.mkString("[", ",", "]")})"
}
