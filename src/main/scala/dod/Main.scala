package dod

import dod.game.GameState
import dod.game.gameobject.{GameObject, GameObjectRepository}
import dod.game.gameobject.commons.{CommonsAccessor, CommonsProperty}
import dod.game.gameobject.position.{Coordinates, Direction, PositionProperty}
import dod.game.temporal.Timer
import dod.game.temporal.Timestamps.Timestamp

import java.util.UUID
import scala.collection.immutable.Queue
import scala.util.chaining.scalaUtilChainingOps

object Main {
    def main(args: Array[String]): Unit = {

        val gameObject = new GameObject(
            commonsProperty = CommonsProperty(id = UUID.randomUUID(), name = "TestGameObject", creationTimestamp = Timestamp(0)),
            positionProperty = Some(PositionProperty(coordinates = Coordinates(0, 0), direction = Direction.North, positionTimestamp = Timestamp(0)))
        )

        val gameObjectRepository = GameObjectRepository(Seq(gameObject))

        val gameState = new GameState(gameObjectRepository = gameObjectRepository, events = Queue.empty)

        println(gameState)




        //            .tap(_.positionAccessor.coordinates.pipe(println))
        //            .tap { gm =>
        //                val accessor = gm.commonsAccessor
        //                println(accessor.name)
        //            }
        //
        //        Timer()
        //            .tap(println)
        //            .pipe(_.started)
        //            .tap(println)
        //            .tap(_ => Thread.sleep(1000))
        //            .tap(println)


    }

}
