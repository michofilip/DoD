package dod.service.event

import dod.game.event.BehaviorEvent
import dod.game.gameobject.GameObjectRepository
import dod.game.gameobject.behavior.BehaviorTransformer
import dod.service.event.EventService.EventResponse

import java.util.UUID

private[event] final class BehaviorEventService {

    def processBehaviorEvent(gameObjectRepository: GameObjectRepository, behaviorEvent: BehaviorEvent): EventResponse = behaviorEvent match {
        case BehaviorEvent.UseBehavior(gameObjectId, behaviorKey) =>
            handleUseBehavior(gameObjectRepository, gameObjectId, behaviorKey)

        case BehaviorEvent.AddBehavior(gameObjectId, behaviorKey, behavior) =>
            handleBehaviorUpdate(gameObjectRepository, gameObjectId, BehaviorTransformer.addBehavior(behaviorKey, behavior))

        case BehaviorEvent.RemoveBehavior(gameObjectId, behaviorKey) =>
            handleBehaviorUpdate(gameObjectRepository, gameObjectId, BehaviorTransformer.removeBehavior(behaviorKey))

        case BehaviorEvent.RemoveAllBehavior(gameObjectId) =>
            handleBehaviorUpdate(gameObjectRepository, gameObjectId, BehaviorTransformer.removeAllBehavior)
    }

    private inline def handleUseBehavior(gameObjectRepository: GameObjectRepository, gameObjectId: UUID, behaviorKey: String): EventResponse =
        gameObjectRepository.findBehavior(gameObjectId, behaviorKey).fold((gameObjectRepository, Seq.empty)) { behavior =>
            (gameObjectRepository, behavior.events)
        }

    private inline def handleBehaviorUpdate(gameObjectRepository: GameObjectRepository,
                                            gameObjectId: UUID,
                                            behaviorTransformer: BehaviorTransformer): EventResponse =
        gameObjectRepository.findById(gameObjectId).fold((gameObjectRepository, Seq.empty)) { gameObject =>
            (gameObjectRepository - gameObject + gameObject.updateBehaviors(behaviorTransformer), Seq.empty)
        }

}
