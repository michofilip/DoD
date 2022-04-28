package dod.service.event

import dod.game.event.{Event, PositionEvent, SchedulerEvent, ScriptEvent, StateEvent, TimerEvent}
import dod.game.expression.Expr
import dod.game.gameobject.GameObjectRepository
import dod.service.event.EventService.{EventResponse, defaultResponse}
import dod.service.event.{PositionEventService, StateEventService}

import scala.annotation.tailrec
import scala.collection.immutable.Queue

final class EventService {

    private val positionEventService = new PositionEventService
    private val stateEventService = new StateEventService
    private val schedulerService = new SchedulerEventService
    private val timerEventService = new TimerEventService
    private val scriptEventService = new ScriptEventService

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

    inline private def processEvent(gameObjectRepository: GameObjectRepository, event: Event): EventResponse = {
        given GameObjectRepository = gameObjectRepository

        event match {
            case positionEvent: PositionEvent => positionEventService.processPositionEvent(positionEvent)
            case stateEvent: StateEvent => stateEventService.processStateEvent(gameObjectRepository, stateEvent)
            case schedulerEvent: SchedulerEvent => schedulerService.processSchedulerEvent(gameObjectRepository, schedulerEvent)
            case timerEvent: TimerEvent => timerEventService.processTimerEvent(timerEvent)
            case scriptEvent: ScriptEvent => scriptEventService.processScriptEvent(gameObjectRepository, scriptEvent)
            case _ => defaultResponse
        }
    }
}

object EventService {
    type EventResponse = (GameObjectRepository, Seq[Event])

    def defaultResponse(using gameObjectRepository: GameObjectRepository): EventResponse = (gameObjectRepository, Seq.empty)

    extension[T] (using GameObjectRepository)(expr: Expr[T]) {
        private[event] def ~>(f: T => EventResponse): EventResponse = handle1(expr)(f)
    }

    extension[T1, T2] (using GameObjectRepository)(pair: (Expr[T1], Expr[T2])) {
        private[event] def ~>(f: (T1, T2) => EventResponse): EventResponse = handle2(pair._1, pair._2)(f)
    }

    inline private[event] def handle1[T](e: Expr[T])(f: T => EventResponse)(using GameObjectRepository): EventResponse = {
        for (e <- e.get) yield f(e)
    }.getOrElse(defaultResponse)

    inline private[event] def handle2[T1, T2](e1: Expr[T1], e2: Expr[T2])(f: (T1, T2) => EventResponse)(using GameObjectRepository): EventResponse = {
        for (e1 <- e1.get; e2 <- e2.get) yield f(e1, e2)
    }.getOrElse(defaultResponse)
}
