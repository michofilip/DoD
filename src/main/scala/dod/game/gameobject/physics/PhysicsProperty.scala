package dod.game.gameobject.physics

import dod.game.gameobject.state.State

final class PhysicsProperty(physicsSelector: PhysicsSelector) {
    def physics(state: Option[State]): Physics = physicsSelector.selectPhysics(state)
}
