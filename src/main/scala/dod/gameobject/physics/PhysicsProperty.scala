package dod.gameobject.physics

import dod.gameobject.state.State

final class PhysicsProperty(physicsSelector: PhysicsSelector) {
    def physics(state: Option[State]): Physics = physicsSelector.selectPhysics(state)
}
