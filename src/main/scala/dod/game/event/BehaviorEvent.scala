package dod.game.event

import dod.game.model.Behavior

import java.util.UUID

enum BehaviorEvent extends Event {
    case UseBehavior(gameObjectId: UUID, behaviorName: String)
    case AddBehavior(gameObjectId: UUID, behaviorName: String, behavior: Behavior)
    case RemoveBehavior(gameObjectId: UUID, behaviorName: String)
    case RemoveAllBehavior(gameObjectId: UUID)
}
