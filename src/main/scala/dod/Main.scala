package dod

import dod.gameobject.GameObject
import dod.gameobject.commons.{CommonsAccessor, CommonsProperty}
import dod.temporal.Timestamps.Timestamp

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
    }

}
