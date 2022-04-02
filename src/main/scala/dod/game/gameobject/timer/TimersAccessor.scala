package dod.game.gameobject.timer

import dod.game.temporal.Durations.Duration
import dod.game.temporal.Timer
import dod.game.temporal.Timestamps.Timestamp

trait TimersAccessor {
    def timer(key: String): Option[Timer]
}
