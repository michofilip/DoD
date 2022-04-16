package dod.service

import dod.data.PhysicsSelectorRepository
import dod.game.gameobject.*
import dod.game.gameobject.behavior.BehaviorProperty
import dod.game.gameobject.commons.*
import dod.game.gameobject.graphics.*
import dod.game.gameobject.physics.*
import dod.game.gameobject.position.*
import dod.game.gameobject.scheduler.SchedulerProperty
import dod.game.gameobject.script.ScriptProperty
import dod.game.gameobject.state.StateTransformer
import dod.game.gameobject.timer.{TimersProperty, TimersTransformer}
import dod.game.model.Timestamps.Timestamp
import dod.game.model.{Coordinates, Direction}

import java.util.UUID

class GameObjectService(positionService: PositionService,
                        stateService: StateService,
                        physicsService: PhysicsService,
                        graphicsService: GraphicsService) {

    private def createGameObject(id: UUID, name: String, timestamp: Timestamp): GameObject = {
        GameObject(
            commonsProperty = CommonsProperty(id = id, name = name, creationTimestamp = timestamp),
            positionProperty = positionService.getPositionProperty(name, timestamp),
            stateProperty = stateService.getStateProperty(name, timestamp),
            physicsProperty = physicsService.getPhysicsProperty(name),
            graphicsProperty = graphicsService.getGraphicsProperty(name),
            timersProperty = Some(TimersProperty()),
            schedulerProperty = Some(SchedulerProperty()),
            behaviorProperty = Some(BehaviorProperty()),
            scriptProperty = Some(ScriptProperty())
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
