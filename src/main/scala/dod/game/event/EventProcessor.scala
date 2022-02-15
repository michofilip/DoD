package dod.game.event

import dod.game.gameobject.position.{Coordinates, PositionTransformer}
import dod.game.gameobject.{GameObject, GameObjectRepository}
import dod.game.temporal.Timestamps
import dod.game.temporal.Timestamps.Timestamp

import java.util.UUID
import scala.util.chaining.scalaUtilChainingOps

class EventProcessor {

    type EventResponse = (GameObjectRepository, Seq[Event])

    def processEvent(event: Event, gameObjectRepository: GameObjectRepository): EventResponse = event match {
        case Event.MoveTo(gameObjectId, coordinates) =>
            handlePositionChange(gameObjectRepository, gameObjectId, PositionTransformer.moveTo(coordinates))

        //        case Event.MoveBy(gameObjectId, shift) => ???
        //        case Event.StartTimer => ???
        //        case Event.StopTimer => ???
        //        case Event.Remove(gameObjectIds) => ???
    }

    inline private def handlePositionChange(gameObjectRepository: GameObjectRepository, gameObjectId: UUID, positionTransformer: PositionTransformer): EventResponse = {
        def canPositionChange(gameObject: GameObject, gameObjectUpdated: GameObject) =
            gameObject.positionAccessor.coordinates == gameObjectUpdated.positionAccessor.coordinates
                || !gameObjectUpdated.positionAccessor.coordinates.exists(gameObjectRepository.existSolidAtCoordinates)

        gameObjectRepository.findById(gameObjectId).map { gameObject =>
            (gameObject, gameObject.updatePosition(positionTransformer, Timestamp(0)))
        }.collect { case (gameObject, gameObjectUpdated) if canPositionChange(gameObject, gameObjectUpdated) =>
            gameObjectRepository - gameObject + gameObjectUpdated
        }.getOrElse(gameObjectRepository).pipe { gameObjectRepository =>
            (gameObjectRepository, Seq.empty)
        }
    }

}
