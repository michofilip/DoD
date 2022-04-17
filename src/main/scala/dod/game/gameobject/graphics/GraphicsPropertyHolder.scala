package dod.game.gameobject.graphics

import dod.game.gameobject.GameObject
import dod.game.gameobject.commons.CommonsPropertyHolder
import dod.game.gameobject.position.PositionPropertyHolder
import dod.game.gameobject.state.StatePropertyHolder
import dod.game.model.Durations.Duration
import dod.game.model.Frame
import dod.game.model.Timestamps.Timestamp

private[gameobject] trait GraphicsPropertyHolder {
    self: GameObject with CommonsPropertyHolder with StatePropertyHolder with PositionPropertyHolder =>

    protected val graphicsProperty: Option[GraphicsProperty]

    final val graphics = new GraphicsAccessor {
        override def layer: Option[Int] = self.graphicsProperty.map(_.animation(states.state, position.direction).layer)

        override def length: Option[Duration] = self.graphicsProperty.map(_.animation(states.state, position.direction).length)

        override def frame(timestamp: Timestamp): Option[Frame] =
            self.graphicsProperty.map { graphicsProperty =>
                val initialTimestamp = states.stateTimestamp.getOrElse(self.creationTimestamp)
                val duration = Duration.between(initialTimestamp, timestamp)

                graphicsProperty.animation(states.state, position.direction).frame(duration)
            }
    }

    final def withGraphicsProperty(graphicsProperty: GraphicsProperty): GameObject =
        if (self.graphicsProperty.isEmpty)
            update(graphicsProperty = Some(graphicsProperty))
        else this

}
