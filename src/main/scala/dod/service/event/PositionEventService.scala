package dod.service.event

import dod.game.event.{Event, PositionEvent}
import dod.game.gameobject.position.PositionTransformer
import dod.game.gameobject.{GameObject, GameObjectRepository}
import dod.game.temporal.Timestamps.Timestamp
import dod.service.event.EventService.EventResponse

import java.util.UUID
import scala.util.chaining.scalaUtilChainingOps

private[event] final class PositionEventService {

    def processPositionEvent(gameObjectRepository: GameObjectRepository, positionEvent: PositionEvent): EventResponse = positionEvent match {
        case PositionEvent.MoveTo(gameObjectId, coordinates) =>
            handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.moveTo(coordinates))

        case PositionEvent.MoveBy(gameObjectId, shift) =>
            handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.moveBy(shift))

        case PositionEvent.TurnTo(gameObjectId, direction) =>
            handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.turnTo(direction))

        case PositionEvent.TurnClockwise(gameObjectId) =>
            handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.turnClockwise)

        case PositionEvent.TurnCounterClockwise(gameObjectId) =>
            handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.turnCounterClockwise)

        case PositionEvent.TurnBack(gameObjectId) =>
            handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.turnBack)

        case PositionEvent.Step(gameObjectId, direction) =>
            handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.step(direction))

        case PositionEvent.StepForward(gameObjectId) =>
            handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.stepForward)

        case PositionEvent.StepRight(gameObjectId) =>
            handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.stepRight)

        case PositionEvent.StepLeft(gameObjectId) =>
            handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.stepLeft)

        case PositionEvent.StepBack(gameObjectId) =>
            handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.stepBack)

        case PositionEvent.StepAndFace(gameObjectId, direction) =>
            handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.stepAndFace(direction))

        case PositionEvent.StepRightAndFace(gameObjectId) =>
            handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.stepRightAndFace)

        case PositionEvent.StepLeftAndFace(gameObjectId) =>
            handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.stepLeftAndFace)

        case PositionEvent.StepBackAndFace(gameObjectId) =>
            handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.stepBackAndFace)
    }


    private def handlePositionChange(gameObjectRepository: GameObjectRepository, gameObjectId: UUID, positionTransformer: PositionTransformer): EventResponse = {
        gameObjectRepository.findById(gameObjectId).map { gameObject =>
            (gameObjectRepository - gameObject, gameObject.updatePosition(positionTransformer, gameObjectRepository.globalTimestamp))
        }.collect { case (gameObjectRepository, gameObject) if canUpdatePosition(gameObjectRepository, gameObject) =>
            (gameObjectRepository + gameObject, Seq.empty)
        }.getOrElse {
            (gameObjectRepository, Seq.empty)
        }
    }


    inline private def canUpdatePosition(gameObjectRepository: GameObjectRepository, gameObjectUpdated: GameObject): Boolean =
        !gameObjectUpdated.positionAccessor.coordinates.exists(gameObjectRepository.existSolidAtCoordinates)

}
