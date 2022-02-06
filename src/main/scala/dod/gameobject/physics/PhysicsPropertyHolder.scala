package dod.gameobject.physics

import dod.gameobject.GameObject
import dod.gameobject.state.StatePropertyHolder

private[gameobject] trait PhysicsPropertyHolder {
    self: GameObject with StatePropertyHolder =>

    protected val physicsProperty: Option[PhysicsProperty]

    final val physicsData = new PhysicsData {
        def physics: Option[Physics] = self.physicsProperty.map(_.physics(stateData.state))
    }
}
