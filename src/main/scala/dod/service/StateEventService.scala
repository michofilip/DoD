package dod.service

import dod.game.event.StateEvent
import dod.game.gameobject.state.StateTransformer
import dod.game.gameobject.{GameObject, GameObjectRepository}
import dod.service.EventService.EventResponse

import java.util.UUID

private[service] final class StateEventService {

    def processStateEvent(gameObjectRepository: GameObjectRepository, stateEvent: StateEvent): EventResponse = stateEvent match {
        case StateEvent.SwitchOff(gameObjectId) =>
            handleStateChange(gameObjectRepository, gameObjectId, StateTransformer.switchOff)

        case StateEvent.SwitchOn(gameObjectId) =>
            handleStateChange(gameObjectRepository, gameObjectId, StateTransformer.switchOn)

        case StateEvent.Switch(gameObjectId) =>
            handleStateChange(gameObjectRepository, gameObjectId, StateTransformer.switch)

        case StateEvent.Open(gameObjectId) =>
            handleStateChange(gameObjectRepository, gameObjectId, StateTransformer.open)

        case StateEvent.Close(gameObjectId) =>
            handleStateChange(gameObjectRepository, gameObjectId, StateTransformer.close)
    }

    private def handleStateChange(gameObjectRepository: GameObjectRepository, gameObjectId: UUID, stateTransformer: StateTransformer): EventResponse = {
        gameObjectRepository.findById(gameObjectId).map { gameObject =>
            (gameObjectRepository - gameObject, gameObject.updateState(stateTransformer, gameObjectRepository.globalTimestamp))
        }.collect { case (gameObjectRepository, gameObject) if canUpdateState(gameObjectRepository, gameObject) =>
            (gameObjectRepository + gameObject, Seq.empty)
        }.getOrElse {
            (gameObjectRepository, Seq.empty)
        }
    }

    inline private def canUpdateState(gameObjectRepository: GameObjectRepository, gameObjectUpdated: GameObject): Boolean =
        !gameObjectUpdated.positionAccessor.coordinates.exists(gameObjectRepository.existSolidAtCoordinates)
}
