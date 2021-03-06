package dod.game.gameobject.timer

import dod.game.gameobject.GameObject
import dod.game.model.{Duration, Timer, Timestamp}

import java.sql.Time

trait TimersPropertyHolder {
    self: GameObject =>

    protected val timersProperty: Option[TimersProperty]

    final def timer(timerName: String): Option[Timer] = self.timersProperty.flatMap(_.timers.get(timerName))

    final def updateTimers(timersTransformer: TimersTransformer): GameObject =
        update(timersProperty = self.timersProperty.map(_.updateTimers(timersTransformer)))

    final def withTimersProperty(): GameObject =
        if (self.timersProperty.isEmpty)
            update(timersProperty = Some(TimersProperty()))
        else this

}
