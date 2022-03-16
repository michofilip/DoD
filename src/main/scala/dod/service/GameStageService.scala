package dod.service

import dod.game.GameStage
import dod.game.event.Event
import dod.game.gameobject.GameObjectRepository
import dod.game.gameobject.position.{Coordinates, Direction, Shift}
import dod.game.temporal.Timestamps.Timestamp

import java.util.UUID
import scala.collection.immutable.Queue

class GameStageService(gameObjectService: GameObjectService) {

    def getGameStage: GameStage = {
        val floors = for {
            x <- -5 to 5
            y <- -5 to 5
            coordinates = Coordinates(x, y)
        } yield gameObjectService.createFloor(UUID.randomUUID(), Timestamp.zero, coordinates)

        val walls = for {
            x <- -5 to 5
            y <- -5 to 5
            if x == -5 || x == 5 || y == -5 || y == 5
            coordinates = Coordinates(x, y)
        } yield gameObjectService.createWall(UUID.randomUUID(), Timestamp.zero, coordinates)

        val player = gameObjectService.createPlayer(UUID.randomUUID(), Timestamp.zero, Coordinates(0, 0), Direction.North)
        val globalTimer = gameObjectService.crateGlobalTimer(UUID.randomUUID(), Timestamp.zero)

        val gameObjects = floors ++ walls ++ Seq(player) ++ Seq(globalTimer)

        val gameObjectRepository = GameObjectRepository(gameObjects)
            .addByName("player", player.commonsAccessor.id)
            .addByName("global_timer", globalTimer.commonsAccessor.id)

        new GameStage(gameObjectRepository, Queue.empty)
    }
}
