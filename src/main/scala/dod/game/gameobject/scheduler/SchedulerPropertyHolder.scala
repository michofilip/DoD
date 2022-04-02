package dod.game.gameobject.scheduler

import dod.game.event.Event
import dod.game.gameobject.GameObject
import dod.game.temporal.Durations.Duration

import java.util.UUID

trait SchedulerPropertyHolder {
    self: GameObject =>

    protected val schedulerProperty: Option[SchedulerProperty]

    final def scheduler(key: String): Option[Scheduler] = self.schedulerProperty.flatMap(_.schedulers.get(key))

    final def updateSchedulers(schedulerTransformer: SchedulerTransformer): GameObject =
        update(schedulerProperty = self.schedulerProperty.map(_.updateSchedulers(schedulerTransformer)))

}
