package dod.service.event

import dod.game.event.{Event, SchedulerEvent}
import dod.game.gameobject.GameObjectRepository
import dod.game.gameobject.scheduler.SchedulerTransformer
import dod.game.model.Durations.Duration
import dod.game.model.Timer
import dod.service.event.EventService.EventResponse

import java.util.UUID

private[event] final class SchedulerEventService {

    private[event] def processSchedulerEvent(gameObjectRepository: GameObjectRepository, schedulerEvent: SchedulerEvent): EventResponse = schedulerEvent match {
        case SchedulerEvent.CheckScheduler(gameObjectId, schedulerName) =>
            handleCheckScheduler(gameObjectRepository, gameObjectId, schedulerName)

        case SchedulerEvent.ScheduleOnce(gameObjectId, schedulerName, timerId, timerKey, delay, events) =>
            handleScheduleOnce(gameObjectRepository, gameObjectId, schedulerName, timerId, timerKey, delay, events)

        case SchedulerEvent.ScheduleAtFixedRate(gameObjectId, schedulerName, timerId, timerKey, delay, events) =>
            handleScheduleAtFixedRate(gameObjectRepository, gameObjectId, schedulerName, timerId, timerKey, delay, events)

        case SchedulerEvent.RemoveScheduler(gameObjectId, schedulerName) =>
            handleSchedulerUpdate(gameObjectRepository, gameObjectId, SchedulerTransformer.removeScheduler(schedulerName))

        case SchedulerEvent.DelayScheduler(gameObjectId, schedulerName, duration) =>
            handleSchedulerUpdate(gameObjectRepository, gameObjectId, SchedulerTransformer.delaySchedulerBy(schedulerName, duration))
    }

    private inline def handleCheckScheduler(gameObjectRepository: GameObjectRepository, gameObjectId: UUID, schedulerName: String): EventResponse = {
        import SchedulerEvent.*

        for
            scheduler <- gameObjectRepository.findScheduler(gameObjectId, schedulerName)
            timer <- gameObjectRepository.findTimer(scheduler.timerId, scheduler.timerKey)
            ready = timer.durationSince(scheduler.initialTimeStamp) >= scheduler.delay
        yield
            val events =
                if ready && scheduler.repeating then
                    DelayScheduler(gameObjectId, schedulerName, scheduler.delay) +: CheckScheduler(gameObjectId, schedulerName) +: scheduler.events
                else if ready && !scheduler.repeating then
                    RemoveScheduler(gameObjectId, schedulerName) +: scheduler.events
                else
                    Seq(CheckScheduler(gameObjectId, schedulerName))

            (gameObjectRepository, events)

    }.getOrElse {
        (gameObjectRepository, Seq.empty)
    }

    private inline def handleScheduleOnce(gameObjectRepository: GameObjectRepository, gameObjectId: UUID, schedulerName: String, timerId: UUID, timerKey: String, delay: Duration, events: Seq[Event]): EventResponse = {
        import SchedulerEvent.*

        for
            timer <- gameObjectRepository.findTimer(timerId, timerKey)
        yield
            val schedulerTransformer = SchedulerTransformer.scheduleOnce(schedulerName, timerId, timerKey, timer.timestamp, delay, events)
            val responseEvents = Seq(CheckScheduler(gameObjectId, schedulerName))

            handleSchedulerUpdate(gameObjectRepository, gameObjectId, schedulerTransformer, responseEvents)

    }.getOrElse {
        (gameObjectRepository, Seq.empty)
    }

    private inline def handleScheduleAtFixedRate(gameObjectRepository: GameObjectRepository, gameObjectId: UUID, schedulerName: String, timerId: UUID, timerKey: String, delay: Duration, events: Seq[Event]): EventResponse = {
        import SchedulerEvent.*

        for
            timer <- gameObjectRepository.findTimer(timerId, timerKey)
        yield
            val schedulerTransformer = SchedulerTransformer.scheduleAtFixedRate(schedulerName, timerId, timerKey, timer.timestamp, delay, events)
            val responseEvents = Seq(CheckScheduler(gameObjectId, schedulerName))

            handleSchedulerUpdate(gameObjectRepository, gameObjectId, schedulerTransformer, responseEvents)

    }.getOrElse {
        (gameObjectRepository, Seq.empty)
    }

    private inline def handleSchedulerUpdate(gameObjectRepository: GameObjectRepository,
                                             gameObjectId: UUID,
                                             schedulerTransformer: SchedulerTransformer,
                                             events: Seq[Event] = Seq.empty): EventResponse =
        gameObjectRepository.findById(gameObjectId).fold((gameObjectRepository, Seq.empty)) { gameObject =>
            (gameObjectRepository - gameObject + gameObject.updateSchedulers(schedulerTransformer), events)
        }

}
