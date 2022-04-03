package dod.game.event

import dod.game.model.Behavior

import java.util.UUID

enum BehaviorEvent extends Event {
    case UseBehavior(gameObjectId: UUID, behaviorKey: String)
    case AddBehavior(gameObjectId: UUID, behaviorKey: String, behavior: Behavior)
    case RemoveBehavior(gameObjectId: UUID, behaviorKey: String)
    case RemoveAllBehavior(gameObjectId: UUID)
}
