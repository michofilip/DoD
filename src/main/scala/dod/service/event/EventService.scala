package dod.service.event

import dod.game.event.{Event, ExpressionEvent, PositionEvent, SchedulerEvent, ScriptEvent, StateEvent, TimerEvent}
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
    private val expressionEventService = new ExpressionEventService

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
            case stateEvent: StateEvent => stateEventService.processStateEvent(stateEvent)
            case schedulerEvent: SchedulerEvent => schedulerService.processSchedulerEvent(schedulerEvent)
            case timerEvent: TimerEvent => timerEventService.processTimerEvent(timerEvent)
            case scriptEvent: ScriptEvent => scriptEventService.processScriptEvent(scriptEvent)
            case expressionEvent: ExpressionEvent => expressionEventService.processExpressionEvent(expressionEvent)
            case _ => defaultResponse
        }
    }
}

object EventService {
    type EventResponse = (GameObjectRepository, Queue[Event])

    private[event] def defaultResponse(using gameObjectRepository: GameObjectRepository): EventResponse = (gameObjectRepository, Queue.empty)

    extension[T] (using GameObjectRepository)(expr: Expr[T]) {
        private[event] def ~>(f: T => EventResponse): EventResponse = handle1(expr)(f)
    }

    extension[T1, T2] (using GameObjectRepository)(tuple: (Expr[T1], Expr[T2])) {
        private[event] def ~>(f: (T1, T2) => EventResponse): EventResponse = handle2(tuple._1, tuple._2)(f)
    }

    extension[T1, T2, T3] (using GameObjectRepository)(tuple: (Expr[T1], Expr[T2], Expr[T3])) {
        private[event] def ~>(f: (T1, T2, T3) => EventResponse): EventResponse = handle3(tuple._1, tuple._2, tuple._3)(f)
    }

    extension[T1, T2, T3, T4] (using GameObjectRepository)(tuple: (Expr[T1], Expr[T2], Expr[T3], Expr[T4])) {
        private[event] def ~>(f: (T1, T2, T3, T4) => EventResponse): EventResponse = handle4(tuple._1, tuple._2, tuple._3, tuple._4)(f)
    }

    extension[T1, T2, T3, T4, T5] (using GameObjectRepository)(tuple: (Expr[T1], Expr[T2], Expr[T3], Expr[T4], Expr[T5])) {
        private[event] def ~>(f: (T1, T2, T3, T4, T5) => EventResponse): EventResponse = handle5(tuple._1, tuple._2, tuple._3, tuple._4, tuple._5)(f)
    }

    inline private def handle1[T](e: Expr[T])(f: T => EventResponse)(using GameObjectRepository): EventResponse = {
        for (e <- e.get) yield f(e)
    }.getOrElse(defaultResponse)

    inline private def handle2[T1, T2](e1: Expr[T1], e2: Expr[T2])(f: (T1, T2) => EventResponse)(using GameObjectRepository): EventResponse = {
        for (e1 <- e1.get; e2 <- e2.get) yield f(e1, e2)
    }.getOrElse(defaultResponse)

    inline private def handle3[T1, T2, T3](e1: Expr[T1], e2: Expr[T2], e3: Expr[T3])(f: (T1, T2, T3) => EventResponse)(using GameObjectRepository): EventResponse = {
        for (e1 <- e1.get; e2 <- e2.get; e3 <- e3.get) yield f(e1, e2, e3)
    }.getOrElse(defaultResponse)

    inline private def handle4[T1, T2, T3, T4](e1: Expr[T1], e2: Expr[T2], e3: Expr[T3], e4: Expr[T4])(f: (T1, T2, T3, T4) => EventResponse)(using GameObjectRepository): EventResponse = {
        for (e1 <- e1.get; e2 <- e2.get; e3 <- e3.get; e4 <- e4.get) yield f(e1, e2, e3, e4)
    }.getOrElse(defaultResponse)

    inline private def handle5[T1, T2, T3, T4, T5](e1: Expr[T1], e2: Expr[T2], e3: Expr[T3], e4: Expr[T4], e5: Expr[T5])(f: (T1, T2, T3, T4, T5) => EventResponse)(using GameObjectRepository): EventResponse = {
        for (e1 <- e1.get; e2 <- e2.get; e3 <- e3.get; e4 <- e4.get; e5 <- e5.get) yield f(e1, e2, e3, e4, e5)
    }.getOrElse(defaultResponse)
}
