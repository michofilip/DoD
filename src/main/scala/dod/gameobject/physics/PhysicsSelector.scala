package dod.gameobject.physics

import dod.gameobject.state.State

final class PhysicsSelector(variants: Map[Option[State], Physics]) {
    inline def selectPhysics(state: Option[State]): Physics = variants(state)
}

object PhysicsSelector {
    def apply(variants: (Option[State], Physics)*): PhysicsSelector =
        new PhysicsSelector(variants.toMap)
}
