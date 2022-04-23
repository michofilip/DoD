package dod.service.event

import dod.game.event.BehaviorEvent
import dod.game.gameobject.GameObjectRepository
import dod.game.gameobject.behavior.BehaviorTransformer
import dod.service.event.EventService.EventResponse

import java.util.UUID

private[event] final class BehaviorEventService {

    def processBehaviorEvent(gameObjectRepository: GameObjectRepository, behaviorEvent: BehaviorEvent): EventResponse = behaviorEvent match {
        case BehaviorEvent.UseBehavior(gameObjectId, behaviorName) =>
            handleUseBehavior(gameObjectRepository, gameObjectId, behaviorName)

        case BehaviorEvent.AddBehavior(gameObjectId, behaviorName, behavior) =>
            handleBehaviorUpdate(gameObjectRepository, gameObjectId, BehaviorTransformer.addBehavior(behaviorName, behavior))

        case BehaviorEvent.RemoveBehavior(gameObjectId, behaviorName) =>
            handleBehaviorUpdate(gameObjectRepository, gameObjectId, BehaviorTransformer.removeBehavior(behaviorName))

        case BehaviorEvent.RemoveAllBehavior(gameObjectId) =>
            handleBehaviorUpdate(gameObjectRepository, gameObjectId, BehaviorTransformer.removeAllBehavior)
    }

    private inline def handleUseBehavior(gameObjectRepository: GameObjectRepository, gameObjectId: String, behaviorName: String): EventResponse =
        gameObjectRepository.findBehavior(gameObjectId, behaviorName).fold((gameObjectRepository, Seq.empty)) { behavior =>
            (gameObjectRepository, behavior.events)
        }

    private inline def handleBehaviorUpdate(gameObjectRepository: GameObjectRepository,
                                            gameObjectId: String,
                                            behaviorTransformer: BehaviorTransformer): EventResponse =
        gameObjectRepository.findById(gameObjectId).fold((gameObjectRepository, Seq.empty)) { gameObject =>
            (gameObjectRepository - gameObject + gameObject.updateBehaviors(behaviorTransformer), Seq.empty)
        }

}
