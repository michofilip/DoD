package dod.game.event

import dod.game.gameobject.position.{Coordinates, Direction, PositionTransformer}
import dod.game.gameobject.{GameObject, GameObjectRepository}
import dod.game.temporal.Timestamps
import dod.game.temporal.Timestamps.Timestamp

import java.util.UUID
import scala.annotation.tailrec
import scala.collection.immutable.Queue
import scala.util.chaining.scalaUtilChainingOps

class EventProcessor {

    type EventResponse = (GameObjectRepository, Seq[Event])

    def processEvents(gameObjectRepository: GameObjectRepository, events: Queue[Event]): EventResponse = {
        @tailrec
        def pe(gameObjectRepository: GameObjectRepository, events: Queue[Event], responseEvents: Queue[Event]): EventResponse = events match
            case event +: rest => processEvent(gameObjectRepository, event) match
                case (gameObjectRepository, events) => pe(gameObjectRepository, rest, responseEvents ++ events)
            case _ => (gameObjectRepository, responseEvents)

        pe(gameObjectRepository, events, Queue.empty)
    }


    inline private def handlePositionChange(gameObjectRepository: GameObjectRepository, gameObjectId: UUID, positionTransformer: PositionTransformer): EventResponse = {
        gameObjectRepository.findById(gameObjectId).map { gameObject =>
            (gameObjectRepository - gameObject, gameObject.updatePosition(positionTransformer, Timestamp(0)))
        }.collect { case (gameObjectRepository, gameObjectUpdated) if canChangePosition(gameObjectRepository, gameObjectUpdated) =>
            gameObjectRepository + gameObjectUpdated
        }.getOrElse(gameObjectRepository).pipe { gameObjectRepository =>
            (gameObjectRepository, Seq.empty)
        }
    }


    inline private def canChangePosition(gameObjectRepository: GameObjectRepository, gameObjectUpdated: GameObject): Boolean =
        !gameObjectUpdated.positionAccessor.coordinates.exists(gameObjectRepository.existSolidAtCoordinates)


    inline private def processEvent(gameObjectRepository: GameObjectRepository, event: Event): EventResponse = event match {
        case Event.MoveTo(gameObjectId, coordinates) => handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.moveTo(coordinates))
        case Event.MoveBy(gameObjectId, shift) => handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.moveBy(shift))
        case Event.TurnTo(gameObjectId, direction) => handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.turnTo(direction))
        case Event.TurnClockwise(gameObjectId) => handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.turnClockwise)
        case Event.TurnCounterClockwise(gameObjectId) => handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.turnCounterClockwise)
        case Event.TurnBack(gameObjectId) => handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.turnBack)
        case Event.Step(gameObjectId, direction) => handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.step(direction))
        case Event.StepForward(gameObjectId) => handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.stepForward)
        case Event.StepRight(gameObjectId) => handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.stepRight)
        case Event.StepLeft(gameObjectId) => handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.stepLeft)
        case Event.StepBack(gameObjectId) => handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.stepBack)
        case Event.StepAndFace(gameObjectId, direction) => handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.stepAndFace(direction))
        case Event.StepRightAndFace(gameObjectId) => handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.stepRightAndFace)
        case Event.StepLeftAndFace(gameObjectId) => handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.stepLeftAndFace)
        case Event.StepBackAndFace(gameObjectId) => handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.stepBackAndFace)
    }

}
