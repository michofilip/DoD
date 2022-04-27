package dod.game.gameobject

import dod.game.expression.Expr.ExprContext
import dod.game.model.Timestamps.Timestamp
import dod.game.model.{Coordinates, Scheduler, Script, Timer}

import java.util.UUID
import scala.annotation.targetName
import scala.util.chaining.scalaUtilChainingOps

class GameObjectRepository private(gameObjectsById: Map[String, GameObject],
                                   gameObjectsByCoordinates: Map[Coordinates, Map[String, GameObject]]) extends ExprContext {

    @targetName("add")
    def +(gameObject: GameObject): GameObjectRepository = {
        val newGameObjectsByCoordinates = gameObject.position.coordinates.fold(gameObjectsByCoordinates) { coordinates =>
            val newGameObjectsAtCoordinates = gameObjectsByCoordinates.getOrElse(coordinates, Map.empty) + (gameObject.id -> gameObject)

            gameObjectsByCoordinates + (coordinates -> newGameObjectsAtCoordinates)
        }

        val newGameObjectsById = gameObjectsById + (gameObject.id -> gameObject)

        new GameObjectRepository(newGameObjectsById, newGameObjectsByCoordinates)
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

        new GameObjectRepository(newGameObjectsById, newGameObjectsByCoordinates)
    }

    @targetName("removeAll")
    def --(gameObjects: Seq[GameObject]): GameObjectRepository =
        gameObjects.foldLeft(this)(_ - _)

    def findAll: Seq[GameObject] =
        gameObjectsById.values.toSeq

    def findById(id: String): Option[GameObject] =
        gameObjectsById.get(id)

    def findByCoordinates(coordinates: Coordinates): Map[String, GameObject] =
        gameObjectsByCoordinates.getOrElse(coordinates, Map.empty)

    def existAtCoordinates(coordinates: Coordinates)(predicate: GameObject => Boolean): Boolean =
        findByCoordinates(coordinates).values.exists(predicate)

    def forallAtCoordinates(coordinates: Coordinates)(predicate: GameObject => Boolean): Boolean =
        findByCoordinates(coordinates).values.forall(predicate)

    def existSolidAtCoordinates(coordinates: Coordinates): Boolean = existAtCoordinates(coordinates) { gameObject =>
        gameObject.physics.fold(false)(_.solid)
    }

    def findTimer(id: String, timerName: String): Option[Timer] =
        findById(id).flatMap(_.timer(timerName))

    def findScheduler(id: String, schedulerName: String): Option[Scheduler] =
        findById(id).flatMap(_.scheduler(schedulerName))

    def findScript(id: String, scriptName: String): Option[Script] =
        findById(id).flatMap(_.script(scriptName))

}

object GameObjectRepository {
    def apply(gameObjects: Seq[GameObject] = Seq.empty): GameObjectRepository =
        new GameObjectRepository(Map.empty, Map.empty) ++ gameObjects
}
