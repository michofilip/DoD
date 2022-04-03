package dod.game.gameobject.behavior

import dod.game.model.Behavior

final class BehaviorProperty(private[behavior] val behaviors: Map[String, Behavior] = Map.empty) {

    def updateBehaviors(behaviorTransformer: BehaviorTransformer): BehaviorProperty = {
        new BehaviorProperty(behaviorTransformer(behaviors))
    }

}
