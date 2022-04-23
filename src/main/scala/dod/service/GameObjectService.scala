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

    extension[T] (t: T) {
        private def conditionalUpdate(condition: Boolean)(f: T => T): T = if (condition) f(t) else t

        private def optionalUpdate[U](option: Option[U])(f: (T, U) => T): T = option.fold(t)(f(t, _))
    }

    private def createGameObject(id: String, name: String, timestamp: Timestamp): GameObject = {
        val positionProperty = positionService.getPositionProperty(name, timestamp)
        val stateProperty = stateService.getStateProperty(name, timestamp)
        val physicsProperty = physicsService.getPhysicsProperty(name)
        val graphicsProperty = graphicsService.getGraphicsProperty(name)

        GameObject(id = id, name = name, creationTimestamp = timestamp)
            .optionalUpdate(positionProperty)(_ withPositionProperty _)
            .optionalUpdate(stateProperty)(_ withStateProperty _)
            .optionalUpdate(physicsProperty)(_ withPhysicsProperty _)
            .optionalUpdate(graphicsProperty)(_ withGraphicsProperty _)
    }

    def createFloor(id: String, timestamp: Timestamp, coordinates: Coordinates): GameObject = {
        createGameObject(id, "floor", timestamp)
            .updatePosition(PositionTransformer.moveTo(coordinates), timestamp)
    }

    def createWall(id: String, timestamp: Timestamp, coordinates: Coordinates): GameObject = {
        createGameObject(id, "wall", timestamp)
            .updatePosition(PositionTransformer.moveTo(coordinates), timestamp)
    }

    def createPlayer(id: String, timestamp: Timestamp, coordinates: Coordinates, direction: Direction): GameObject = {
        createGameObject(id, "player", timestamp)
            .updatePosition(PositionTransformer.moveTo(coordinates), timestamp)
            .updatePosition(PositionTransformer.turnTo(direction), timestamp)
    }

    def createDoor(id: String, timestamp: Timestamp, coordinates: Coordinates, closed: Boolean): GameObject = {
        createGameObject(id, "door", timestamp)
            .updatePosition(PositionTransformer.moveTo(coordinates), timestamp)
            .conditionalUpdate(closed)(_.updateState(StateTransformer.close, timestamp))
    }

    def createSwitch(id: String, timestamp: Timestamp, coordinates: Coordinates, on: Boolean): GameObject = {
        createGameObject(id, "switch", timestamp)
            .updatePosition(PositionTransformer.moveTo(coordinates), timestamp)
            .conditionalUpdate(on)(_.updateState(StateTransformer.switchOn, timestamp))
    }

    def crateGlobalTimer(id: String, timestamp: Timestamp): GameObject = {
        createGameObject(id, "global_timer", timestamp)
            .withTimersProperty()
            .updateTimers(TimersTransformer.addTimerAndStart("timer_1", Timestamp.zero))
    }
}
