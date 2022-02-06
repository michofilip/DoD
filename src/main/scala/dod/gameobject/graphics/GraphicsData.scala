package dod.gameobject.graphics

import dod.temporal.Durations.Duration
import dod.temporal.Timestamps.Timestamp

trait GraphicsData {
    def level: Option[Int]

    def length: Option[Duration]

    def frame(timestamp: Timestamp): Option[Frame]
}
