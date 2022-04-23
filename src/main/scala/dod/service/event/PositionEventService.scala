package dod.service.event

import dod.game.event.{Event, PositionEvent}
import dod.game.gameobject.position.PositionTransformer
import dod.game.gameobject.{GameObject, GameObjectRepository}
import dod.game.model.Timestamps.Timestamp
import dod.service.event.EventService.EventResponse

import java.util.UUID
import scala.util.chaining.scalaUtilChainingOps

private[event] final class PositionEventService {

    def processPositionEvent(gameObjectRepository: GameObjectRepository, positionEvent: PositionEvent): EventResponse = positionEvent match {
        case PositionEvent.MoveTo(gameObjectId, coordinates) =>
            handlePositionUpdate(gameObjectRepository, gameObjectId, PositionTransformer.moveTo(coordinates))

        case PositionEvent.MoveBy(gameObjectId, shift) =>
            handlePositionUpdate(gameObjectRepository, gameObjectId, PositionTransformer.moveBy(shift))

        case PositionEvent.TurnTo(gameObjectId, direction) =>
            handlePositionUpdate(gameObjectRepository, gameObjectId, PositionTransformer.turnTo(direction))

        case PositionEvent.TurnClockwise(gameObjectId) =>
            handlePositionUpdate(gameObjectRepository, gameObjectId, PositionTransformer.turnClockwise)

        case PositionEvent.TurnCounterClockwise(gameObjectId) =>
            handlePositionUpdate(gameObjectRepository, gameObjectId, PositionTransformer.turnCounterClockwise)

        case PositionEvent.TurnBack(gameObjectId) =>
            handlePositionUpdate(gameObjectRepository, gameObjectId, PositionTransformer.turnBack)

        case PositionEvent.Step(gameObjectId, direction) =>
            handlePositionUpdate(gameObjectRepository, gameObjectId, PositionTransformer.step(direction))

        case PositionEvent.StepForward(gameObjectId) =>
            handlePositionUpdate(gameObjectRepository, gameObjectId, PositionTransformer.stepForward)

        case PositionEvent.StepRight(gameObjectId) =>
            handlePositionUpdate(gameObjectRepository, gameObjectId, PositionTransformer.stepRight)

        case PositionEvent.StepLeft(gameObjectId) =>
            handlePositionUpdate(gameObjectRepository, gameObjectId, PositionTransformer.stepLeft)

        case PositionEvent.StepBack(gameObjectId) =>
            handlePositionUpdate(gameObjectRepository, gameObjectId, PositionTransformer.stepBack)

        case PositionEvent.StepAndFace(gameObjectId, direction) =>
            handlePositionUpdate(gameObjectRepository, gameObjectId, PositionTransformer.stepAndFace(direction))

        case PositionEvent.StepRightAndFace(gameObjectId) =>
            handlePositionUpdate(gameObjectRepository, gameObjectId, PositionTransformer.stepRightAndFace)

        case PositionEvent.StepLeftAndFace(gameObjectId) =>
            handlePositionUpdate(gameObjectRepository, gameObjectId, PositionTransformer.stepLeftAndFace)

        case PositionEvent.StepBackAndFace(gameObjectId) =>
            handlePositionUpdate(gameObjectRepository, gameObjectId, PositionTransformer.stepBackAndFace)
    }


    private def handlePositionUpdate(gameObjectRepository: GameObjectRepository, gameObjectId: String, positionTransformer: PositionTransformer): EventResponse = {
        gameObjectRepository.findById(gameObjectId).map { gameObject =>
            val timestamp = gameObjectRepository.findTimer("global_timers", "timer_1").fold(Timestamp.zero)(_.timestamp)

            (gameObjectRepository - gameObject, gameObject.updatePosition(positionTransformer, timestamp))
        }.collect { case (gameObjectRepository, gameObject) if canUpdatePosition(gameObjectRepository, gameObject) =>
            (gameObjectRepository + gameObject, Seq.empty)
        }.getOrElse {
            (gameObjectRepository, Seq.empty)
        }
    }


    inline private def canUpdatePosition(gameObjectRepository: GameObjectRepository, gameObjectUpdated: GameObject): Boolean =
        !gameObjectUpdated.position.coordinates.exists(gameObjectRepository.existSolidAtCoordinates)

}
