package dod.game.event

import dod.game.model.Behavior

enum BehaviorEvent extends Event {
    case UseBehavior(gameObjectId: String, behaviorName: String)
    case AddBehavior(gameObjectId: String, behaviorName: String, behavior: Behavior)
    case RemoveBehavior(gameObjectId: String, behaviorName: String)
    case RemoveAllBehavior(gameObjectId: String)
}
