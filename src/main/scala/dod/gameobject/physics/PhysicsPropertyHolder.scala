package dod.gameobject.physics

import dod.gameobject.GameObject
import dod.gameobject.state.StatePropertyHolder

private[gameobject] trait PhysicsPropertyHolder[T <: GameObject] {
    this: StatePropertyHolder[T] =>

    protected val physicsProperty: Option[PhysicsProperty]

    final val physicsData = new PhysicsData {
        def physics: Option[Physics] = physicsProperty.map(_.physics(stateData.state))
    }
}
