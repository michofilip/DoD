package dod.game.model

import dod.game.model.Duration
import dod.game.model.Timestamp

class Timer private(initialTimestamp: Timestamp, lastStartTimestamp: Option[Timestamp]) {

    def running: Boolean = lastStartTimestamp.isDefined

    def started: Timer =
        if running then this
        else new Timer(initialTimestamp, Option(Timestamp.now))

    def stopped: Timer =
        if running then new Timer(timestamp, None)
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
        if running then new Timer(initialTimestamp, Option(Timestamp.now))
        else new Timer(initialTimestamp, None)
}