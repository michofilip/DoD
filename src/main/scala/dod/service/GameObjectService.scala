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
import scala.util.chaining.scalaUtilChainingOps

class GameObjectService(positionService: PositionService,
                        stateService: StateService,
                        physicsService: PhysicsService,
                        graphicsService: GraphicsService) {

    extension[T] (t: T) {
        private def conditionalUpdate(condition: Boolean)(f: T => T): T = t.pipe(t => if (condition) f(t) else t)
    }

    private def createGameObject(id: UUID, name: String, timestamp: Timestamp): GameObject = {
        GameObject(id = id, name = name, creationTimestamp = timestamp)
            .withPositionProperty(positionService.getPositionProperty(name, timestamp))
            .withStateProperty(stateService.getStateProperty(name, timestamp))
            .withPhysicsProperty(physicsService.getPhysicsProperty(name))
            .withGraphicsProperty(graphicsService.getGraphicsProperty(name))
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
        createGameObject(id, "door", timestamp)
            .updatePosition(PositionTransformer.moveTo(coordinates), timestamp)
            .conditionalUpdate(closed)(_.updateState(StateTransformer.close, timestamp))
    }

    def createSwitch(id: UUID, timestamp: Timestamp, coordinates: Coordinates, on: Boolean): GameObject = {
        createGameObject(id, "switch", timestamp)
            .updatePosition(PositionTransformer.moveTo(coordinates), timestamp)
            .conditionalUpdate(on)(_.updateState(StateTransformer.switchOn, timestamp))
    }

    def crateGlobalTimer(id: UUID, timestamp: Timestamp): GameObject = {
        createGameObject(id, "global_timer", timestamp)
            .withTimersProperty()
            .updateTimers(TimersTransformer.addTimerAndStart("global_timer_1", Timestamp.zero))
    }
}
