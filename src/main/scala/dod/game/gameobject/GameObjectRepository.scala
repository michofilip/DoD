package dod.game.gameobject

import dod.game.gameobject.position.Coordinates

import java.util.UUID
import scala.annotation.targetName
import scala.util.chaining.scalaUtilChainingOps

class GameObjectRepository private(gameObjectsById: Map[UUID, GameObject],
                                   gameObjectsByCoordinates: Map[Coordinates, Map[UUID, GameObject]]) {

    @targetName("add")
    def +(gameObject: GameObject): GameObjectRepository = {
        val newGameObjectsByCoordinates = gameObject.positionAccessor.coordinates.fold(gameObjectsByCoordinates) { coordinates =>
            val newGameObjectsAtCoordinates = gameObjectsByCoordinates.getOrElse(coordinates, Map.empty) + (gameObject.commonsAccessor.id -> gameObject)

            gameObjectsByCoordinates + (coordinates -> newGameObjectsAtCoordinates)
        }

        val newGameObjectsById = gameObjectsById + (gameObject.commonsAccessor.id -> gameObject)

        new GameObjectRepository(newGameObjectsById, newGameObjectsByCoordinates)
    }

    @targetName("addAll")
    def ++(gameObjects: Seq[GameObject]): GameObjectRepository =
        gameObjects.foldLeft(this)(_ + _)

    @targetName("remove")
    def -(gameObject: GameObject): GameObjectRepository = {
        val newGameObjectsByCoordinates = gameObject.positionAccessor.coordinates.fold(gameObjectsByCoordinates) { coordinates =>
            val newGameObjectsAtCoordinates = gameObjectsByCoordinates.getOrElse(coordinates, Map.empty) - gameObject.commonsAccessor.id

            if (newGameObjectsAtCoordinates.isEmpty)
                gameObjectsByCoordinates - coordinates
            else
                gameObjectsByCoordinates + (coordinates -> newGameObjectsAtCoordinates)
        }

        val newGameObjectsById = gameObjectsById - gameObject.commonsAccessor.id

        new GameObjectRepository(newGameObjectsById, newGameObjectsByCoordinates)
    }

    @targetName("removeAll")
    def --(gameObjects: Seq[GameObject]): GameObjectRepository =
        gameObjects.foldLeft(this)(_ - _)

    def findAll: Seq[GameObject] =
        gameObjectsById.values.toSeq

    def findById(id: UUID): Option[GameObject] =
        gameObjectsById.get(id)

    def findByCoordinates(coordinates: Coordinates): Map[UUID, GameObject] =
        gameObjectsByCoordinates.getOrElse(coordinates, Map.empty)

    def existAtCoordinates(coordinates: Coordinates)(predicate: GameObject => Boolean): Boolean =
        findByCoordinates(coordinates).values.exists(predicate)

    def forallAtCoordinates(coordinates: Coordinates)(predicate: GameObject => Boolean): Boolean =
        findByCoordinates(coordinates).values.forall(predicate)

    def existSolidAtCoordinates(coordinates: Coordinates): Boolean = existAtCoordinates(coordinates) { gameObject =>
        gameObject.physicsAccessor.physics.fold(false)(_.solid)
    }

    override def toString: String = findAll.mkString("[", ", ", "]")

}

object GameObjectRepository {
    def apply(gameObjects: Seq[GameObject] = Seq.empty): GameObjectRepository =
        new GameObjectRepository(Map.empty, Map.empty) ++ gameObjects
}
