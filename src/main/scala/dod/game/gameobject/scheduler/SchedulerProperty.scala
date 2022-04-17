package dod.game.gameobject.scheduler

import dod.game.model.Scheduler

final class SchedulerProperty(private[scheduler] val schedulers: Map[String, Scheduler] = Map.empty) {

    def updateSchedulers(schedulerTransformer: SchedulerTransformer): SchedulerProperty = {
        new SchedulerProperty(schedulerTransformer(schedulers))
    }

}
