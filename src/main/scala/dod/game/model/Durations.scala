package dod.game.model

import dod.game.model.Timestamps.Timestamp

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

        @targetName("less")
        def <(that: Duration): Boolean = value.compare(that) < 0

        @targetName("lessEq")
        def <=(that: Duration): Boolean = value.compare(that) <= 0

        @targetName("greater")
        def >(that: Duration): Boolean = value.compare(that) > 0

        @targetName("greaterEq")
        def >=(that: Duration): Boolean = value.compare(that) >= 0
    }

    given Ordering[Duration] with {
        override def compare(x: Duration, y: Duration): Int = java.lang.Long.compare(x.value, y.value)
    }
}
