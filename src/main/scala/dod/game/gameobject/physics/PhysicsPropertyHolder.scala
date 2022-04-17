package dod.game.gameobject.physics

import dod.game.gameobject.GameObject
import dod.game.gameobject.state.StatePropertyHolder
import dod.game.model.Physics

private[gameobject] trait PhysicsPropertyHolder {
    self: GameObject with StatePropertyHolder =>

    protected val physicsProperty: Option[PhysicsProperty]

    final def physics: Option[Physics] = self.physicsProperty.map(_.physics(states.state))

    final def withPhysicsProperty(physicsProperty: PhysicsProperty): GameObject =
        if (self.physicsProperty.isEmpty)
            update(physicsProperty = Some(physicsProperty))
        else this
}
