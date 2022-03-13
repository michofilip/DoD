package dod.game.temporal

import dod.game.temporal.Durations.Duration
import dod.game.temporal.Timestamps.Timestamp

class Timer private(initialTimestamp: Timestamp,
                    lastStartTimestamp: Option[Timestamp]) {

    val running: Boolean = lastStartTimestamp.isDefined

    def started: Timer =
        if (running) this
        else new Timer(initialTimestamp, Option(Timestamp.now))

    def stopped: Timer =
        if (running) new Timer(timestamp, None)
        else this

    def timestamp: Timestamp =
        lastStartTimestamp.fold(initialTimestamp) { lastStartTimestamp =>
            initialTimestamp + Duration.between(lastStartTimestamp, Timestamp.now)
        }

    def duration: Duration = durationSince(Timestamp.zero)

    def durationSince(timestamp: Timestamp): Duration = Duration.between(timestamp, this.timestamp)
    
}

object Timer {
    def apply(initialTimestamp: Timestamp = Timestamp.zero, running: Boolean = false): Timer =
        if (running) new Timer(initialTimestamp, Option(Timestamp.now))
        else new Timer(initialTimestamp, None)
}