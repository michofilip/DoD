package dod.service

import dod.data.{AnimationSelectorRepository, PhysicsSelectorRepository}
import dod.game.gameobject.graphics.GraphicsProperty
import dod.game.gameobject.physics.PhysicsProperty

class GraphicsService(animationSelectorRepository: AnimationSelectorRepository) {
    def getGraphicsProperty(name: String): Option[GraphicsProperty] = {
        animationSelectorRepository.findByName(name).map { animationSelector =>
            new GraphicsProperty(animationSelector)
        }
    }
}
