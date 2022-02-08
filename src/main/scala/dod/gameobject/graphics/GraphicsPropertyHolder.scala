package dod.gameobject.graphics

import dod.gameobject.GameObject
import dod.gameobject.commons.CommonsPropertyHolder
import dod.gameobject.position.PositionPropertyHolder
import dod.gameobject.state.StatePropertyHolder
import dod.temporal.Durations.Duration
import dod.temporal.Timestamps.Timestamp

private[gameobject] trait GraphicsPropertyHolder {
    self: GameObject with CommonsPropertyHolder with StatePropertyHolder with PositionPropertyHolder =>

    protected val graphicsProperty: Option[GraphicsProperty]

    final val graphicsAccessor = new GraphicsAccessor {
        override def level: Option[Int] = self.graphicsProperty.map(_.level)

        override def length: Option[Duration] = self.graphicsProperty.map(_.animation(stateAccessor.state, positionAccessor.direction).length)

        override def frame(timestamp: Timestamp): Option[Frame] =
            self.graphicsProperty.map { graphicsProperty =>
                val initialTimestamp = stateAccessor.stateTimestamp.getOrElse(commonsAccessor.creationTimestamp)
                val duration = Duration.between(initialTimestamp, timestamp)

                graphicsProperty.animation(stateAccessor.state, positionAccessor.direction).frame(duration)
            }
    }

}
