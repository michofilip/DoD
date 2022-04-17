package dod.game.gameobject.behavior

import dod.game.gameobject.GameObject
import dod.game.model.Behavior

trait BehaviorPropertyHolder {
    self: GameObject =>

    protected val behaviorProperty: Option[BehaviorProperty]

    final def behavior(behaviorName: String): Option[Behavior] = self.behaviorProperty.flatMap(_.behaviors.get(behaviorName))

    final def updateBehaviors(behaviorTransformer: BehaviorTransformer): GameObject =
        update(behaviorProperty = self.behaviorProperty.map(_.updateBehaviors(behaviorTransformer)))

    final def withBehaviorProperty(): GameObject =
        if (self.behaviorProperty.isEmpty)
            update(behaviorProperty = Some(BehaviorProperty()))
        else this

}
