package dod.service

import dod.data.PhysicsSelectorRepository
import dod.game.gameobject.physics.PhysicsProperty

class PhysicsService(physicsSelectorRepository: PhysicsSelectorRepository) {
    def getPhysicsProperty(name: String): Option[PhysicsProperty] = {
        physicsSelectorRepository.findByName(name).map { physicsSelector =>
            new PhysicsProperty(physicsSelector)
        }
    }
}
