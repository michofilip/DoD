package dod.game.gameobject.timer

import dod.game.gameobject.GameObject
import dod.game.temporal.Durations.Duration
import dod.game.temporal.Timer
import dod.game.temporal.Timestamps.Timestamp

trait TimersPropertyHolder {
    self: GameObject =>

    protected val timersProperty: Option[TimersProperty]

    final val timersAccessor = new TimersAccessor {
        override def timer(key: String): Option[Timer] = self.timersProperty.flatMap(_.timers.get(key))

        override def running(key: String): Option[Boolean] = self.timersProperty.flatMap(_.timers.get(key)).map(_.running)

        override def timestamp(key: String): Option[Timestamp] = self.timersProperty.flatMap(_.timers.get(key)).map(_.timestamp)

        override def duration(key: String): Option[Duration] = self.timersProperty.flatMap(_.timers.get(key)).map(_.duration)

        override def durationSince(key: String, timestamp: Timestamp): Option[Duration] = self.timersProperty.flatMap(_.timers.get(key)).map(_.durationSince(timestamp))
    }

    final def updateTimers(timersTransformer: TimersTransformer): GameObject =
        update(timersProperty = self.timersProperty.map(_.updateTimers(timersTransformer)))

}
