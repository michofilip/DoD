package dod.game.gameobject.graphics

import dod.game.gameobject.GameObject
import dod.game.gameobject.commons.CommonsPropertyHolder
import dod.game.gameobject.position.PositionPropertyHolder
import dod.game.gameobject.state.StatePropertyHolder
import dod.game.temporal.Durations.Duration
import dod.game.temporal.Timestamps.Timestamp

private[gameobject] trait GraphicsPropertyHolder {
    self: GameObject with CommonsPropertyHolder with StatePropertyHolder with PositionPropertyHolder =>

    protected val graphicsProperty: Option[GraphicsProperty]

    final val graphicsAccessor = new GraphicsAccessor {
        override def layer: Option[Int] = self.graphicsProperty.map(_.animation(stateAccessor.state, positionAccessor.direction).layer)

        override def length: Option[Duration] = self.graphicsProperty.map(_.animation(stateAccessor.state, positionAccessor.direction).length)

        override def frame(timestamp: Timestamp): Option[Frame] =
            self.graphicsProperty.map { graphicsProperty =>
                val initialTimestamp = stateAccessor.stateTimestamp.getOrElse(commonsAccessor.creationTimestamp)
                val duration = Duration.between(initialTimestamp, timestamp)

                graphicsProperty.animation(stateAccessor.state, positionAccessor.direction).frame(duration)
            }
    }

}
