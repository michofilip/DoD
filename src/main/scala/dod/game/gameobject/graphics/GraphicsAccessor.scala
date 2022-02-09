package dod.game.gameobject.graphics

import dod.game.temporal.Durations.Duration
import dod.game.temporal.Timestamps.Timestamp

trait GraphicsAccessor {
    def level: Option[Int]

    def length: Option[Duration]

    def frame(timestamp: Timestamp): Option[Frame]
}
