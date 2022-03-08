package dod.game.gameobject.physics

import dod.game.gameobject.state.State

final class PhysicsSelector(variants: Map[Option[State], Physics]) {
    inline def selectPhysics(state: Option[State]): Physics = variants(state)
}

// TODO not needed anymore
object PhysicsSelector {
    def apply(variants: (Option[State], Physics)*): PhysicsSelector =
        new PhysicsSelector(variants.toMap)
}
