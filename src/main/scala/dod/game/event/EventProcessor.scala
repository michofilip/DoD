package dod.game.event

import dod.game.gameobject.position.{Coordinates, PositionTransformer}
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

    def processEvent(gameObjectRepository: GameObjectRepository, event: Event): EventResponse = event match
        case Event.MoveTo(gameObjectId, coordinates) => handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.moveTo(coordinates))
        case Event.MoveBy(gameObjectId, shift) => handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.moveBy(shift))


    inline private def handlePositionChange(gameObjectRepository: GameObjectRepository, gameObjectId: UUID, positionTransformer: PositionTransformer): EventResponse = {
        def canPositionChange(gameObjectRepository: GameObjectRepository, gameObjectUpdated: GameObject) =
            !gameObjectUpdated.positionAccessor.coordinates.exists(gameObjectRepository.existSolidAtCoordinates)

        gameObjectRepository.findById(gameObjectId).map { gameObject =>
            (gameObjectRepository - gameObject, gameObject.updatePosition(positionTransformer, Timestamp(0)))
        }.collect { case (gameObjectRepository, gameObjectUpdated) if canPositionChange(gameObjectRepository, gameObjectUpdated) =>
            gameObjectRepository + gameObjectUpdated
        }.getOrElse(gameObjectRepository).pipe { gameObjectRepository =>
            (gameObjectRepository, Seq.empty)
        }
    }

}
