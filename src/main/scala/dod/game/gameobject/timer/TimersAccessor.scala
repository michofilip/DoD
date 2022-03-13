package dod.game.gameobject.timer

import dod.game.temporal.Durations.Duration
import dod.game.temporal.Timer
import dod.game.temporal.Timestamps.Timestamp

trait TimersAccessor {
    def running(key: String): Option[Boolean]

    def timestamp(key: String): Option[Timestamp]

    def duration(key: String): Option[Duration]

    def durationSince(key: String, timestamp: Timestamp): Option[Duration]
}
