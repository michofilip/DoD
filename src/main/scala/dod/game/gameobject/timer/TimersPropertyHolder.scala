package dod.game.gameobject.timer

import dod.game.gameobject.GameObject
import dod.game.model.Durations.Duration
import dod.game.model.Timer
import dod.game.model.Timestamps.Timestamp

trait TimersPropertyHolder {
    self: GameObject =>

    protected val timersProperty: Option[TimersProperty]

    final def timer(key: String): Option[Timer] = self.timersProperty.flatMap(_.timers.get(key))

    final def updateTimers(timersTransformer: TimersTransformer): GameObject =
        update(timersProperty = self.timersProperty.map(_.updateTimers(timersTransformer)))

}
