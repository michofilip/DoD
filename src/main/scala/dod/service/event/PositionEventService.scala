package dod.service.event

import dod.game.event.{Event, PositionEvent}
import dod.game.expression.Expr
import dod.game.gameobject.position.PositionTransformer
import dod.game.gameobject.{GameObject, GameObjectRepository}
import dod.game.model.Timestamps.Timestamp
import dod.service.event.EventService.*

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
