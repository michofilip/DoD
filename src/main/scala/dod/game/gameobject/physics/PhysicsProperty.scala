package dod.game.gameobject.physics

import dod.game.model.{Physics, PhysicsSelector, State}

final class PhysicsProperty(physicsSelector: PhysicsSelector) {
    def physics(state: Option[State]): Physics = physicsSelector.selectPhysics(state)
}
