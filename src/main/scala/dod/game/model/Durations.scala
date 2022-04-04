package dod.game.model

import Timestamps.Timestamp

import scala.annotation.targetName

object Durations {
    opaque type Duration = Long

    object Duration {
        def apply(milliseconds: Long): Duration = milliseconds

        def between(from: Timestamp, to: Timestamp): Duration = to.value - from.value

        def zero: Duration = Duration(0)
    }

    extension (value: Long) {
        def milliseconds: Duration = value
    }

    extension (value: Double) {
        def milliseconds: Duration = value.toLong
    }

    extension (duration: Duration) {
        def value: Long = duration

        @targetName("positive")
        def unary_+ : Duration = duration

        @targetName("negative")
        def unary_- : Duration = -duration

        @targetName("add")
        def +(that: Duration): Duration = duration + that

        @targetName("subtract")
        def -(that: Duration): Duration = duration - that

        def >(that: Duration): Boolean = value > that.value

        def >=(that: Duration): Boolean = value >= that.value

        def <(that: Duration): Boolean = value < that.value

        def <=(that: Duration): Boolean = value <= that.value
    }
}