package dod.service

import dod.data.PositionRepository
import dod.game.gameobject.physics.PhysicsProperty
import dod.game.gameobject.position.PositionProperty
import dod.game.model.Timestamps.Timestamp

class PositionService(positionRepository: PositionRepository) {
    def getPositionProperty(name: String, timestamp: Timestamp): Option[PositionProperty] = {
        positionRepository.findByName(name).map { position =>
            new PositionProperty(position, timestamp)
        }
    }
}
