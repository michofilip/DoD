package dod

import dod.game.gameobject.GameObject
import dod.game.gameobject.commons.{CommonsAccessor, CommonsProperty}
import dod.game.temporal.Timer
import dod.game.temporal.Timestamps.Timestamp

import java.util.UUID
import scala.util.chaining.scalaUtilChainingOps

object Main {
    def main(args: Array[String]): Unit = {
        new GameObject(new CommonsProperty(id = UUID.randomUUID(), name = "TestGameObject", creationTimestamp = Timestamp(0)))
            .tap(_.positionAccessor.coordinates.pipe(println))
            .tap { gm =>
                val accessor = gm.commonsAccessor
                println(accessor.name)
            }

        Timer()
            .tap(println)
            .pipe(_.started)
            .tap(println)
            .tap(_ => Thread.sleep(1000))
            .tap(println)


    }

}
