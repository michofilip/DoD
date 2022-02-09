package dod.game.gameobject.physics

import dod.game.gameobject.GameObject
import dod.game.gameobject.state.StatePropertyHolder

private[gameobject] trait PhysicsPropertyHolder {
    self: GameObject with StatePropertyHolder =>

    protected val physicsProperty: Option[PhysicsProperty]

    final val physicsAccessor = new PhysicsAccessor {
        def physics: Option[Physics] = self.physicsProperty.map(_.physics(stateAccessor.state))
    }
}
