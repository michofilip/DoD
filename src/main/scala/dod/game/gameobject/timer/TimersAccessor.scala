package dod.game.gameobject.timer

import dod.game.temporal.Durations.Duration
import dod.game.temporal.Timer
import dod.game.temporal.Timestamps.Timestamp

trait TimersAccessor {
    def timer(key: String): Option[Timer]

    @Deprecated
    def running(key: String): Option[Boolean]

    @Deprecated
    def timestamp(key: String): Option[Timestamp]

    @Deprecated
    def duration(key: String): Option[Duration]

    @Deprecated
    def durationSince(key: String, timestamp: Timestamp): Option[Duration]
}
