package dod.service

import dod.data.PhysicsSelectorRepository
import dod.game.gameobject.*
import dod.game.gameobject.commons.*
import dod.game.gameobject.graphics.*
import dod.game.gameobject.physics.*
import dod.game.gameobject.position.*
import dod.game.gameobject.state.StateTransformer
import dod.game.gameobject.timer.{TimersProperty, TimersTransformer}
import dod.game.temporal.Timestamps.Timestamp

import java.util.UUID

class GameObjectService(positionService: PositionService,
                        stateService: StateService,
                        physicsService: PhysicsService,
                        graphicsService: GraphicsService) {

    private def createGameObject(id: UUID, name: String, timestamp: Timestamp): GameObject = {
        val commonsProperty = CommonsProperty(id = id, name = name, creationTimestamp = timestamp)
        val positionProperty = positionService.getPositionProperty(name, timestamp)
        val stateProperty = stateService.getStateProperty(name, timestamp)
        val physicsProperty = physicsService.getPhysicsProperty(name)
        val graphicsProperty = graphicsService.getGraphicsProperty(name)
        val timersProperty = Some(TimersProperty())

        GameObject(
            commonsProperty = commonsProperty,
            positionProperty = positionProperty,
            stateProperty = stateProperty,
            physicsProperty = physicsProperty,
            graphicsProperty = graphicsProperty,
            timersProperty = timersProperty
        )
    }

    def createFloor(id: UUID, timestamp: Timestamp, coordinates: Coordinates): GameObject = {
        createGameObject(id, "floor", timestamp)
            .updatePosition(PositionTransformer.moveTo(coordinates), timestamp)
    }

    def createWall(id: UUID, timestamp: Timestamp, coordinates: Coordinates): GameObject = {
        createGameObject(id, "wall", timestamp)
            .updatePosition(PositionTransformer.moveTo(coordinates), timestamp)
    }

    def createPlayer(id: UUID, timestamp: Timestamp, coordinates: Coordinates, direction: Direction): GameObject = {
        createGameObject(id, "player", timestamp)
            .updatePosition(PositionTransformer.moveTo(coordinates), timestamp)
            .updatePosition(PositionTransformer.turnTo(direction), timestamp)
    }

    def createDoor(id: UUID, timestamp: Timestamp, coordinates: Coordinates, closed: Boolean): GameObject = {
        val gameObject = createGameObject(id, "door", timestamp)
            .updatePosition(PositionTransformer.moveTo(coordinates), timestamp)

        if (closed) {
            gameObject.updateState(StateTransformer.close, timestamp)
        } else {
            gameObject
        }
    }

    def createSwitch(id: UUID, timestamp: Timestamp, coordinates: Coordinates, on: Boolean): GameObject = {
        val gameObject = createGameObject(id, "switch", timestamp)
            .updatePosition(PositionTransformer.moveTo(coordinates), timestamp)

        if (on) {
            gameObject.updateState(StateTransformer.switchOn, timestamp)
        } else {
            gameObject
        }
    }

    def crateGlobalTimer(id: UUID, timestamp: Timestamp): GameObject = {
        createGameObject(id, "global_timer", timestamp)
            .updateTimers(TimersTransformer.addTimerAndStart("global_timer_1", Timestamp.zero))
    }
}
