package dod.game.gameobject.scheduler

import dod.game.event.Event
import dod.game.gameobject.GameObject
import dod.game.model.{Duration, Scheduler}

import java.util.UUID

trait SchedulerPropertyHolder {
    self: GameObject =>

    protected val schedulerProperty: Option[SchedulerProperty]

    final def scheduler(key: String): Option[Scheduler] = self.schedulerProperty.flatMap(_.schedulers.get(key))

    final def updateSchedulers(schedulerTransformer: SchedulerTransformer): GameObject =
        update(schedulerProperty = self.schedulerProperty.map(_.updateSchedulers(schedulerTransformer)))

    final def withSchedulerProperty(): GameObject =
        if (self.schedulerProperty.isEmpty)
            update(schedulerProperty = Some(SchedulerProperty()))
        else this

}
