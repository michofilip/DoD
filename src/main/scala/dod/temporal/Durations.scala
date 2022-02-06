package dod.temporal

import dod.temporal.Timestamps.Timestamp
import dod.temporal.Timestamps.Timestamp.*

object Durations {
    opaque type Duration = Long

    object Duration {
        def apply(millis: Long): Duration = millis

        def between(from: Timestamp, to: Timestamp): Duration = to.value - from.value
    }

    extension (value: Long) {
        def milliseconds: Duration = value
    }

    extension (value: Double) {
        def milliseconds: Duration = value.toLong
    }

    extension (duration: Duration) {
        def value: Long = duration

        def unary_+ : Duration = duration

        def unary_- : Duration = -duration

        def +(that: Duration): Duration = duration + that

        def -(that: Duration): Duration = duration - that
    }
}
