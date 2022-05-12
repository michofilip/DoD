package dod.service

import dod.game.GameStage
import dod.game.event.Event
import dod.game.gameobject.GameObjectRepository
import dod.game.model.Timestamps.Timestamp
import dod.game.model.{Coordinates, Direction, Shift}

import java.util.UUID
import scala.annotation.tailrec
import scala.collection.immutable.Queue
import scala.io.{BufferedSource, Source}

class GameStageService(gameObjectService: GameObjectService) {

    def getGameStageTest: (GameStage, Queue[Event]) = {
        val floors = for {
            x <- 0 to 11
            y <- 0 to 11
            coordinates = Coordinates(x, y)
        } yield gameObjectService.createFloor(UUID.randomUUID().toString, Timestamp.zero, coordinates)

        val walls = for {
            x <- 0 to 11
            y <- 0 to 11
            if x == 0 || x == 11 || y == 0 || y == 11
            coordinates = Coordinates(x, y)
        } yield gameObjectService.createWall(UUID.randomUUID().toString, Timestamp.zero, coordinates)

        val player = gameObjectService.createPlayer("player", Timestamp.zero, Coordinates(1, 1), Direction.North)
        val globalTimer = gameObjectService.crateGlobalTimer("global_timer", Timestamp.zero)

        val gameObjects = floors ++ walls ++ Seq(player) ++ Seq(globalTimer)

        val gameObjectRepository = GameObjectRepository(gameObjects)

        (new GameStage(gameObjectRepository), Queue.empty)
    }

    def getGameStageFomMap(mapName: String): (GameStage, Queue[Event]) = {
        val chars = {
            val map: BufferedSource = Source.fromFile(s"assets/maps/$mapName.lvl")
            val chars: Vector[Vector[Char]] = map.getLines().toVector.map(line => line.toVector)

            for {
                x <- 0 until 64
                y <- 0 until 64
            } yield {
                (Coordinates(x, y), chars(y)(x))
            }
        }

        @tailrec
        def f(chars: Seq[(Coordinates, Char)], gameObjectRepository: GameObjectRepository, events: Queue[Event]): (GameObjectRepository, Queue[Event]) = chars match {
            case (coordinates, char) +: rest =>
                getObjects(coordinates, char, gameObjectRepository) match {
                    case (gameObjectRepository, newEvents) => f(rest, gameObjectRepository, events ++ newEvents)
                }

            case _ => (gameObjectRepository, events)
        }

        val (gameObjectRepository, events) = f(chars, GameObjectRepository(), Queue.empty)

        (new GameStage(gameObjectRepository, Some("player")), events)
    }

    private def getObjects(coordinates: Coordinates, char: Char, gameObjectRepository: GameObjectRepository): (GameObjectRepository, Queue[Event]) = {
        val timestamp = Timestamp.zero

        char match {
            case ' ' =>
                (gameObjectRepository, Queue.empty)

            case '.' | 'X' =>
                val floor = gameObjectService.createFloor(UUID.randomUUID().toString, timestamp, coordinates)
                (gameObjectRepository + floor, Queue.empty)

            case '#' =>
                val floor = gameObjectService.createFloor(UUID.randomUUID().toString, timestamp, coordinates)
                val wall = gameObjectService.createWall(UUID.randomUUID().toString, timestamp, coordinates)
                (gameObjectRepository + floor + wall, Queue.empty)

            case '+' =>
                val floor = gameObjectService.createFloor(UUID.randomUUID().toString, timestamp, coordinates)
                val door = gameObjectService.createDoor(UUID.randomUUID().toString, timestamp, coordinates, closed = false)
                (gameObjectRepository + floor + door, Queue.empty)

            case '@' =>
                val floor = gameObjectService.createFloor(UUID.randomUUID().toString, timestamp, coordinates)
                val player = gameObjectService.createPlayer("player", timestamp, coordinates, Direction.East)
                (gameObjectRepository + floor + player, Queue.empty)

            case _ =>
                (gameObjectRepository, Queue.empty)
        }
    }
}
