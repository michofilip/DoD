package dod.service.event

import dod.game.GameStage
import dod.game.event.StateEvent
import dod.game.gameobject.state.StateTransformer
import dod.game.gameobject.{GameObject, GameObjectRepository}
import dod.game.model.Timestamps.Timestamp
import dod.service.event.EventService.*

import java.util.UUID
import scala.collection.immutable.Queue

private[event] final class StateEventService {

    private[event] def processStateEvent(stateEvent: StateEvent)(using gameStage: GameStage): EventResponse = stateEvent match {
        case StateEvent.SwitchOff(gameObjectId) => gameObjectId ~> {
            gameObjectId => handleStateUpdate(gameObjectId, StateTransformer.switchOff)
        }

        case StateEvent.SwitchOn(gameObjectId) => gameObjectId ~> {
            gameObjectId => handleStateUpdate(gameObjectId, StateTransformer.switchOn)
        }

        case StateEvent.Switch(gameObjectId) => gameObjectId ~> {
            gameObjectId => handleStateUpdate(gameObjectId, StateTransformer.switch)
        }

        case StateEvent.Open(gameObjectId) => gameObjectId ~> {
            gameObjectId => handleStateUpdate(gameObjectId, StateTransformer.open)
        }

        case StateEvent.Close(gameObjectId) => gameObjectId ~> {
            gameObjectId => handleStateUpdate(gameObjectId, StateTransformer.close)
        }
    }

    private def handleStateUpdate(gameObjectId: String, stateTransformer: StateTransformer)(using gameStage: GameStage): EventResponse = {
        gameStage.gameObjects.findById(gameObjectId).map { gameObject =>
            (gameStage.updateGameObjects(_ - gameObject), gameObject.updateState(stateTransformer, gameStage.globalTimestamp))
        }.collect { case (gameStage, gameObject) if canUpdateState(gameStage, gameObject) =>
            (gameStage.updateGameObjects(_ + gameObject), Queue.empty)
        }.getOrElse {
            defaultResponse
        }
    }

    inline private def canUpdateState(gameStage: GameStage, gameObjectUpdated: GameObject): Boolean =
        !gameObjectUpdated.position.coordinates.exists(gameStage.gameObjects.existSolidAtCoordinates)
}
