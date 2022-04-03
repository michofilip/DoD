package dod.game.gameobject

import dod.game.model.Timestamps.Timestamp
import dod.game.model.{Behavior, Coordinates, Scheduler, Timer}

import java.util.UUID
import scala.annotation.targetName
import scala.util.chaining.scalaUtilChainingOps

class GameObjectRepository private(gameObjectsById: Map[UUID, GameObject],
                                   gameObjectsByCoordinates: Map[Coordinates, Map[UUID, GameObject]],
                                   gameObjectIdByName: Map[String, UUID]) {

    @targetName("add")
    def +(gameObject: GameObject): GameObjectRepository = {
        val newGameObjectsByCoordinates = gameObject.position.coordinates.fold(gameObjectsByCoordinates) { coordinates =>
            val newGameObjectsAtCoordinates = gameObjectsByCoordinates.getOrElse(coordinates, Map.empty) + (gameObject.id -> gameObject)

            gameObjectsByCoordinates + (coordinates -> newGameObjectsAtCoordinates)
        }

        val newGameObjectsById = gameObjectsById + (gameObject.id -> gameObject)

        new GameObjectRepository(newGameObjectsById, newGameObjectsByCoordinates, gameObjectIdByName)
    }

    @targetName("addAll")
    def ++(gameObjects: Seq[GameObject]): GameObjectRepository =
        gameObjects.foldLeft(this)(_ + _)

    @targetName("remove")
    def -(gameObject: GameObject): GameObjectRepository = {
        val newGameObjectsByCoordinates = gameObject.position.coordinates.fold(gameObjectsByCoordinates) { coordinates =>
            val newGameObjectsAtCoordinates = gameObjectsByCoordinates.getOrElse(coordinates, Map.empty) - gameObject.id

            if (newGameObjectsAtCoordinates.isEmpty)
                gameObjectsByCoordinates - coordinates
            else
                gameObjectsByCoordinates + (coordinates -> newGameObjectsAtCoordinates)
        }

        val newGameObjectsById = gameObjectsById - gameObject.id

        new GameObjectRepository(newGameObjectsById, newGameObjectsByCoordinates, gameObjectIdByName)
    }

    @targetName("removeAll")
    def --(gameObjects: Seq[GameObject]): GameObjectRepository =
        gameObjects.foldLeft(this)(_ - _)

    def addByName(name: String, id: UUID): GameObjectRepository = {
        new GameObjectRepository(gameObjectsById, gameObjectsByCoordinates, gameObjectIdByName + (name -> id))
    }

    def findAll: Seq[GameObject] =
        gameObjectsById.values.toSeq

    def findById(id: UUID): Option[GameObject] =
        gameObjectsById.get(id)

    def findByName(name: String): Option[GameObject] =
        gameObjectIdByName.get(name).flatMap(findById)

    def findByCoordinates(coordinates: Coordinates): Map[UUID, GameObject] =
        gameObjectsByCoordinates.getOrElse(coordinates, Map.empty)

    def existAtCoordinates(coordinates: Coordinates)(predicate: GameObject => Boolean): Boolean =
        findByCoordinates(coordinates).values.exists(predicate)

    def forallAtCoordinates(coordinates: Coordinates)(predicate: GameObject => Boolean): Boolean =
        findByCoordinates(coordinates).values.forall(predicate)

    def existSolidAtCoordinates(coordinates: Coordinates): Boolean = existAtCoordinates(coordinates) { gameObject =>
        gameObject.physics.fold(false)(_.solid)
    }

    @Deprecated
    def globalTimestamp: Timestamp = findByName("global_timers")
        .flatMap(_.timer("global_timer_1"))
        .map(_.timestamp)
        .getOrElse(Timestamp.zero)

    def findTimer(id: UUID, key: String): Option[Timer] =
        findById(id).flatMap(_.timer(key))

    def finsScheduler(id: UUID, key: String): Option[Scheduler] =
        findById(id).flatMap(_.scheduler(key))

    def finsBehavior(id: UUID, key: String): Option[Behavior] =
        findById(id).flatMap(_.behavior(key))
}

object GameObjectRepository {
    def apply(gameObjects: Seq[GameObject] = Seq.empty): GameObjectRepository =
        new GameObjectRepository(Map.empty, Map.empty, Map.empty) ++ gameObjects
}
