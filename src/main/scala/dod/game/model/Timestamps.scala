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
        def value: Long = timestamp

        @targetName("add")
        def +(duration: Duration): Timestamp = timestamp + duration.value

        @targetName("subtract")
        def -(duration: Duration): Timestamp = timestamp - duration.value

        @targetName("less")
        def <(that: Timestamp): Boolean = value.compare(that) < 0

        @targetName("lessEq")
        def <=(that: Timestamp): Boolean = value.compare(that) <= 0

        @targetName("greater")
        def >(that: Timestamp): Boolean = value.compare(that) > 0

        @targetName("greaterEq")
        def >=(that: Timestamp): Boolean = value.compare(that) >= 0
    }

    given Ordering[Timestamp] with {
        override def compare(x: Timestamp, y: Timestamp): Int = java.lang.Long.compare(x.value, y.value)
    }

}
