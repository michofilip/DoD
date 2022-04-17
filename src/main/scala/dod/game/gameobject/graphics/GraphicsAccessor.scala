package dod.game.gameobject.graphics

import dod.game.model.Frame
import dod.game.model.Durations.Duration
import dod.game.model.Timestamps.Timestamp

trait GraphicsAccessor {
    def layer: Option[Int]

    def length: Option[Duration]

    def frame(timestamp: Timestamp): Option[Frame]
}
