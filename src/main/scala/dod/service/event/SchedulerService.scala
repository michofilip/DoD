package dod.service.event

import dod.game.event.{Event, SchedulerEvent}
import dod.game.gameobject.GameObjectRepository
import dod.game.gameobject.scheduler.{Scheduler, SchedulerTransformer}
import dod.game.temporal.Durations.Duration
import dod.game.temporal.Timer
import dod.service.event.EventService.EventResponse

import java.util.UUID

private[event] final class SchedulerService {

    private[event] def processSchedulerEvent(gameObjectRepository: GameObjectRepository, schedulerEvent: SchedulerEvent): EventResponse = schedulerEvent match {
        case SchedulerEvent.CheckScheduler(gameObjectId, schedulerKey) =>
            handleCheckScheduler(gameObjectRepository, gameObjectId, schedulerKey)

        case SchedulerEvent.ScheduleOnce(gameObjectId, schedulerKey, timerId, timerKey, delay, events) =>
            handleScheduleOnce(gameObjectRepository, gameObjectId, schedulerKey, timerId, timerKey, delay, events)

        case SchedulerEvent.ScheduleAtFixedRate(gameObjectId, schedulerKey, timerId, timerKey, delay, events) =>
            handleScheduleAtFixedRate(gameObjectRepository, gameObjectId, schedulerKey, timerId, timerKey, delay, events)

        case SchedulerEvent.RemoveScheduler(gameObjectId, schedulerKey) =>
            handleSchedulerChange(gameObjectRepository, gameObjectId, SchedulerTransformer.remove(schedulerKey))

        case SchedulerEvent.DelayScheduler(gameObjectId, schedulerKey, duration) =>
            handleSchedulerChange(gameObjectRepository, gameObjectId, SchedulerTransformer.delayBy(schedulerKey, duration))
    }

    private inline def handleCheckScheduler(gameObjectRepository: GameObjectRepository, gameObjectId: UUID, schedulerKey: String) = {
        import SchedulerEvent.*

        for
            scheduler <- gameObjectRepository.finsScheduler(gameObjectId, schedulerKey)
            timer <- gameObjectRepository.findTimer(scheduler.timerId, scheduler.timerKey)
            ready = timer.durationSince(scheduler.initialTimeStamp) >= scheduler.delay
        yield
            val events =
                if ready && scheduler.repeating then
                    DelayScheduler(gameObjectId, schedulerKey, scheduler.delay) +: CheckScheduler(gameObjectId, schedulerKey) +: scheduler.events
                else if ready && !scheduler.repeating then
                    RemoveScheduler(gameObjectId, schedulerKey) +: scheduler.events
                else
                    Seq(CheckScheduler(gameObjectId, schedulerKey))

            (gameObjectRepository, events)

    }.getOrElse {
        (gameObjectRepository, Seq.empty)
    }

    private inline def handleScheduleOnce(gameObjectRepository: GameObjectRepository, gameObjectId: UUID, schedulerKey: String, timerId: UUID, timerKey: String, delay: Duration, events: Seq[Event]) = {
        import SchedulerEvent.*

        for
            timer <- gameObjectRepository.findTimer(timerId, timerKey)
        yield
            val schedulerTransformer = SchedulerTransformer.scheduleOnce(schedulerKey, timerId, timerKey, timer.timestamp, delay, events)
            val responseEvents = Seq(CheckScheduler(gameObjectId, schedulerKey))

            handleSchedulerChange(gameObjectRepository, gameObjectId, schedulerTransformer, responseEvents)

    }.getOrElse {
        (gameObjectRepository, Seq.empty)
    }

    private inline def handleScheduleAtFixedRate(gameObjectRepository: GameObjectRepository, gameObjectId: UUID, schedulerKey: String, timerId: UUID, timerKey: String, delay: Duration, events: Seq[Event]) = {
        import SchedulerEvent.*

        for
            timer <- gameObjectRepository.findTimer(timerId, timerKey)
        yield
            val schedulerTransformer = SchedulerTransformer.scheduleAtFixedRate(schedulerKey, timerId, timerKey, timer.timestamp, delay, events)
            val responseEvents = Seq(CheckScheduler(gameObjectId, schedulerKey))

            handleSchedulerChange(gameObjectRepository, gameObjectId, schedulerTransformer, responseEvents)

    }.getOrElse {
        (gameObjectRepository, Seq.empty)
    }

    private inline def handleSchedulerChange(gameObjectRepository: GameObjectRepository,
                                             gameObjectId: UUID,
                                             schedulerTransformer: SchedulerTransformer,
                                             events: Seq[Event] = Seq.empty): EventResponse =
        gameObjectRepository.findById(gameObjectId).fold((gameObjectRepository, Seq.empty)) { gameObject =>
            (gameObjectRepository - gameObject + gameObject.updateSchedulers(schedulerTransformer), events)
        }

}
