package dod.service

import dod.data.PhysicsSelectorRepository
import dod.game.gameobject.*
import dod.game.gameobject.commons.*
import dod.game.gameobject.graphics.*
import dod.game.gameobject.physics.*
import dod.game.gameobject.position.*
import dod.game.temporal.Timestamps.Timestamp

import java.util.UUID

class GameObjectService(positionService: PositionService,
                        stateService: StateService,
                        physicsService: PhysicsService,
                        graphicsService: GraphicsService) {

    def createGameObject(id: UUID, name: String, timestamp: Timestamp): GameObject = {
        val commonsProperty = CommonsProperty(id = id, name = name, creationTimestamp = timestamp)
        val positionProperty = positionService.getPositionProperty(name, timestamp)
        val stateProperty = stateService.getStateProperty(name, timestamp)
        val physicsProperty = physicsService.getPhysicsProperty(name)
        val graphicsProperty = graphicsService.getGraphicsProperty(name)

        GameObject(
            commonsProperty = commonsProperty,
            positionProperty = positionProperty,
            stateProperty = stateProperty,
            physicsProperty = physicsProperty,
            graphicsProperty = graphicsProperty
        )
    }
}
