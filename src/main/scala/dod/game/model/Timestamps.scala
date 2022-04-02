package dod.game.model

import dod.game.model.Durations.Duration

import scala.annotation.targetName

object Timestamps {

    opaque type Timestamp = Long

    object Timestamp {
        def apply(milliseconds: Long): Timestamp = milliseconds

        def zero: Timestamp = Timestamp(0)

        def now: Timestamp = Timestamp(System.currentTimeMillis())
    }

    extension (timestamp: Timestamp) {
        private[model] def value: Long = timestamp

        @targetName("add")
        def +(duration: Duration): Timestamp = timestamp + duration.value

        @targetName("subtract")
        def -(duration: Duration): Timestamp = timestamp - duration.value

        def >(that: Timestamp): Boolean = value > that.value

        def >=(that: Timestamp): Boolean = value >= that.value

        def <(that: Timestamp): Boolean = value < that.value

        def <=(that: Timestamp): Boolean = value <= that.value
    }

}
