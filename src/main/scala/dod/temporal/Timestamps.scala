package dod.temporal

import dod.temporal.Durations.Duration

object Timestamps {

    opaque type Timestamp = Long

    object Timestamp {
        def apply(milliseconds: Long): Timestamp = milliseconds
    }

    extension (timestamp: Timestamp) {
        private[temporal] def value: Long = timestamp

        def +(duration: Duration): Timestamp = timestamp + duration.value
        
        def -(duration: Duration): Timestamp = timestamp - duration.value
    }

}
