package dod.service

import dod.game.event.{Event, PositionEvent}
import dod.game.gameobject.GameObjectRepository
import dod.service.EventService.EventResponse

import scala.annotation.tailrec
import scala.collection.immutable.Queue

class EventService {

    private val positionEventService = new PositionEventService

    def processEvents(gameObjectRepository: GameObjectRepository, events: Queue[Event]): EventResponse = {
        @tailrec
        def pe(gameObjectRepository: GameObjectRepository, events: Queue[Event], responseEvents: Queue[Event]): EventResponse = events match {
            case event +: rest => processEvent(gameObjectRepository, event) match {
                case (gameObjectRepository, events) => pe(gameObjectRepository, rest, responseEvents ++ events)
            }

            case _ => (gameObjectRepository, responseEvents)
        }

        pe(gameObjectRepository, events, Queue.empty)
    }

    inline private def processEvent(gameObjectRepository: GameObjectRepository, event: Event): EventResponse = event match {
        case positionEvent: PositionEvent => positionEventService.processPositionEvent(gameObjectRepository, positionEvent)
        case _ => (gameObjectRepository, Seq.empty)
    }
}

object EventService {
    type EventResponse = (GameObjectRepository, Seq[Event])
}
