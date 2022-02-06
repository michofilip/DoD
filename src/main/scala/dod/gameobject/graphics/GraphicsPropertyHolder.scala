package dod.gameobject.graphics

import dod.gameobject.GameObject
import dod.gameobject.commons.CommonsPropertyHolder
import dod.gameobject.position.PositionPropertyHolder
import dod.gameobject.state.StatePropertyHolder
import dod.temporal.Durations.Duration
import dod.temporal.Timestamps.Timestamp

import scala.util.chaining.scalaUtilChainingOps

private[gameobject] trait GraphicsPropertyHolder[T <: GameObject] {
    this: CommonsPropertyHolder with StatePropertyHolder[T] with PositionPropertyHolder[T] =>

    protected val graphicsProperty: Option[GraphicsProperty]

    final val graphicsData = new GraphicsData {
        override def level: Option[Int] = graphicsProperty.map(_.level)

        override def length: Option[Duration] = graphicsProperty.map(_.animationSelector.selectAnimation(stateData.state, positionData.direction).length)

        override def frame(timestamp: Timestamp): Option[Frame] =
            graphicsProperty.map { graphicsProperty =>
                val initialTimestamp = stateData.stateTimestamp.getOrElse(commonsData.creationTimestamp)
                val duration = Duration.between(initialTimestamp, timestamp)

                graphicsProperty.animationSelector.selectAnimation(stateData.state, positionData.direction).frame(duration)
            }
    }

}
