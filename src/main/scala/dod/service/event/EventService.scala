package dod.service.event

import dod.game.event.{Event, PositionEvent, SchedulerEvent, StateEvent}
import dod.game.gameobject.GameObjectRepository
import dod.service.event.EventService.EventResponse
import dod.service.event.{PositionEventService, StateEventService}

import scala.annotation.tailrec
import scala.collection.immutable.Queue

final class EventService {

    private val positionEventService = new PositionEventService
    private val stateEventService = new StateEventService
    private val schedulerService = new SchedulerService

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
        case stateEvent: StateEvent => stateEventService.processStateEvent(gameObjectRepository, stateEvent)
        case schedulerEvent: SchedulerEvent => schedulerService.processSchedulerEvent(gameObjectRepository, schedulerEvent)
        case _ => (gameObjectRepository, Seq.empty)
    }
}

object EventService {
    type EventResponse = (GameObjectRepository, Seq[Event])
}
