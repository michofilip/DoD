package dod.service.event

import dod.game.event.{Event, SchedulerEvent}
import dod.game.gameobject.GameObjectRepository
import dod.game.gameobject.scheduler.{Scheduler, SchedulerTransformer}
import dod.game.temporal.Durations.Duration
import dod.game.temporal.Timer
import dod.service.event.EventService.EventResponse

import java.util.UUID

private[event] final class SchedulerService {

    def processSchedulerEvent(gameObjectRepository: GameObjectRepository, schedulerEvent: SchedulerEvent): EventResponse = schedulerEvent match {
        case SchedulerEvent.CheckScheduler(gameObjectId, schedulerKey) => {
            for {
                scheduler <- gameObjectRepository.finsScheduler(gameObjectId, schedulerKey)
                timestamp <- gameObjectRepository.getTimestamp(scheduler.timerId, scheduler.timerKey)
            } yield {
                val duration = Duration.between(scheduler.initialTimeStamp, timestamp)
                if (duration >= scheduler.delay) {
                    if (scheduler.repeating) {
                        val schedulerTransformer = SchedulerTransformer.delayBy(schedulerKey, scheduler.delay)
                        val responseEvents = SchedulerEvent.CheckScheduler(gameObjectId, schedulerKey) +: scheduler.events

                        handleSchedulerChange(gameObjectRepository, gameObjectId, schedulerTransformer, responseEvents)
                    } else {
                        val schedulerTransformer = SchedulerTransformer.removeScheduler(schedulerKey)
                        val responseEvents = scheduler.events

                        handleSchedulerChange(gameObjectRepository, gameObjectId, schedulerTransformer, responseEvents)
                    }
                } else {
                    (gameObjectRepository, Seq(SchedulerEvent.CheckScheduler(gameObjectId, schedulerKey)))
                }
            }
        }.getOrElse((gameObjectRepository, Seq.empty))


        case SchedulerEvent.ScheduleOnce(gameObjectId, schedulerKey, timerId, timerKey, delay, events) => {
            for {
                timestamp <- gameObjectRepository.getTimestamp(timerId, timerKey)
            } yield {
                val schedulerTransformer = SchedulerTransformer.scheduleOnce(schedulerKey, timerId, timerKey, timestamp, delay, events)
                val responseEvents = Seq(SchedulerEvent.CheckScheduler(gameObjectId, schedulerKey))

                handleSchedulerChange(gameObjectRepository, gameObjectId, schedulerTransformer, responseEvents)
            }
        }.getOrElse((gameObjectRepository, Seq.empty))


        case SchedulerEvent.ScheduleAtFixedRate(gameObjectId, schedulerKey, timerId, timerKey, delay, events) => {
            for {
                timestamp <- gameObjectRepository.getTimestamp(timerId, timerKey)
            } yield {
                val schedulerTransformer = SchedulerTransformer.scheduleAtFixedRate(schedulerKey, timerId, timerKey, timestamp, delay, events)
                val responseEvents = Seq(SchedulerEvent.CheckScheduler(gameObjectId, schedulerKey))

                handleSchedulerChange(gameObjectRepository, gameObjectId, schedulerTransformer, responseEvents)
            }
        }.getOrElse((gameObjectRepository, Seq.empty))


        case SchedulerEvent.RemoveScheduler(gameObjectId, schedulerKey) =>
            handleSchedulerChange(gameObjectRepository, gameObjectId, SchedulerTransformer.removeScheduler(schedulerKey))
    }

    private inline def handleSchedulerChange(gameObjectRepository: GameObjectRepository,
                                             gameObjectId: UUID,
                                             schedulerTransformer: SchedulerTransformer,
                                             events: Seq[Event] = Seq.empty): EventResponse =
        gameObjectRepository.findById(gameObjectId).fold((gameObjectRepository, Seq.empty)) { gameObject =>
            (gameObjectRepository - gameObject + gameObject.updateSchedulers(schedulerTransformer), events)
        }

}
