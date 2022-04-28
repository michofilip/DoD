package dod.service.event

import dod.game.event.{Event, PositionEvent}
import dod.game.expression.Expr
import dod.game.gameobject.position.PositionTransformer
import dod.game.gameobject.{GameObject, GameObjectRepository}
import dod.game.model.Timestamps.Timestamp
import dod.service.event.EventService.{EventResponse, defaultResponse}

import java.util.UUID
import scala.util.chaining.scalaUtilChainingOps

private[event] final class PositionEventService {

    def processPositionEvent(positionEvent: PositionEvent)(using gameObjectRepository: GameObjectRepository): EventResponse = positionEvent match {
        case PositionEvent.MoveTo(gameObjectId, coordinates) => (gameObjectId, coordinates) ~> {
            (gameObjectId, coordinates) => handlePositionUpdate(gameObjectId, PositionTransformer.moveTo(coordinates))
        }

        case PositionEvent.MoveBy(gameObjectId, shift) => (gameObjectId, shift) ~> {
            (gameObjectId, shift) => handlePositionUpdate(gameObjectId, PositionTransformer.moveBy(shift))
        }

        case PositionEvent.TurnTo(gameObjectId, direction) => (gameObjectId, direction) ~> {
            (gameObjectId, direction) => handlePositionUpdate(gameObjectId, PositionTransformer.turnTo(direction))
        }

        case PositionEvent.TurnClockwise(gameObjectId) => gameObjectId ~> {
            gameObjectId => handlePositionUpdate(gameObjectId, PositionTransformer.turnClockwise)
        }

        case PositionEvent.TurnCounterClockwise(gameObjectId) => gameObjectId ~> {
            gameObjectId => handlePositionUpdate(gameObjectId, PositionTransformer.turnCounterClockwise)
        }

        case PositionEvent.TurnBack(gameObjectId) => gameObjectId ~> {
            gameObjectId => handlePositionUpdate(gameObjectId, PositionTransformer.turnBack)
        }

        case PositionEvent.Step(gameObjectId, direction) => (gameObjectId, direction) ~> {
            (gameObjectId, direction) => handlePositionUpdate(gameObjectId, PositionTransformer.step(direction))
        }

        case PositionEvent.StepForward(gameObjectId) => gameObjectId ~> {
            gameObjectId => handlePositionUpdate(gameObjectId, PositionTransformer.stepForward)
        }

        case PositionEvent.StepRight(gameObjectId) => gameObjectId ~> {
            gameObjectId => handlePositionUpdate(gameObjectId, PositionTransformer.stepRight)
        }

        case PositionEvent.StepLeft(gameObjectId) => gameObjectId ~> {
            gameObjectId => handlePositionUpdate(gameObjectId, PositionTransformer.stepLeft)
        }

        case PositionEvent.StepBack(gameObjectId) => gameObjectId ~> {
            gameObjectId => handlePositionUpdate(gameObjectId, PositionTransformer.stepBack)
        }

        case PositionEvent.StepAndFace(gameObjectId, direction) => (gameObjectId, direction) ~> {
            (gameObjectId, direction) => handlePositionUpdate(gameObjectId, PositionTransformer.stepAndFace(direction))
        }

        case PositionEvent.StepRightAndFace(gameObjectId) => gameObjectId ~> {
            gameObjectId => handlePositionUpdate(gameObjectId, PositionTransformer.stepRightAndFace)
        }

        case PositionEvent.StepLeftAndFace(gameObjectId) => gameObjectId ~> {
            gameObjectId => handlePositionUpdate(gameObjectId, PositionTransformer.stepLeftAndFace)
        }

        case PositionEvent.StepBackAndFace(gameObjectId) => gameObjectId ~> {
            gameObjectId => handlePositionUpdate(gameObjectId, PositionTransformer.stepBackAndFace)
        }
    }

    extension[T] (using GameObjectRepository)(expr: Expr[T]) {
        private def ~>(f: T => EventResponse): EventResponse = handle1(expr)(f)
    }

    extension[T1, T2] (using GameObjectRepository)(pair: (Expr[T1], Expr[T2])) {
        private def ~>(f: (T1, T2) => EventResponse): EventResponse = handle2(pair._1, pair._2)(f)
    }

    inline private def handle1[T](e: Expr[T])(f: T => EventResponse)(using GameObjectRepository): EventResponse = {
        for (e <- e.get) yield f(e)
    }.getOrElse(defaultResponse)

    inline private def handle2[T1, T2](e1: Expr[T1], e2: Expr[T2])(f: (T1, T2) => EventResponse)(using GameObjectRepository): EventResponse = {
        for (e1 <- e1.get; e2 <- e2.get) yield f(e1, e2)
    }.getOrElse(defaultResponse)

    inline private def handlePositionUpdate(gameObjectId: String, positionTransformer: PositionTransformer)(using gameObjectRepository: GameObjectRepository): EventResponse = {
        gameObjectRepository.findById(gameObjectId).map { gameObject =>
            val timestamp = gameObjectRepository.findTimer("global_timers", "timer_1").fold(Timestamp.zero)(_.timestamp)

            (gameObjectRepository - gameObject, gameObject.updatePosition(positionTransformer, timestamp))
        }.collect { case (gameObjectRepository, gameObject) if canUpdatePosition(gameObjectRepository, gameObject) =>
            (gameObjectRepository + gameObject, Seq.empty)
        }.getOrElse {
            defaultResponse
        }
    }

    inline private def canUpdatePosition(gameObjectRepository: GameObjectRepository, gameObjectUpdated: GameObject): Boolean =
        !gameObjectUpdated.position.coordinates.exists(gameObjectRepository.existSolidAtCoordinates)

}
