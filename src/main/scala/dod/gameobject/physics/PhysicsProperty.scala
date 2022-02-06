package dod.gameobject.physics

import dod.gameobject.state.State

final class PhysicsProperty(private[physics] val physicsSelector: PhysicsSelector) {
    def physics(state: Option[State]): Physics =
        physicsSelector.selectPhysics(state)
}
