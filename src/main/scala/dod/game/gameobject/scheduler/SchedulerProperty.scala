package dod.game.gameobject.scheduler

final class SchedulerProperty(private[scheduler] val schedulers: Map[String, Scheduler] = Map.empty) {

    def updateSchedulers(schedulerTransformer: SchedulerTransformer): SchedulerProperty = {
        new SchedulerProperty(schedulerTransformer(schedulers))
    }

}
