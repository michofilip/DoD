package dod.service.event

import dod.game.event.{Event, SchedulerEvent}
import dod.game.expression.Expr
import dod.game.gameobject.GameObjectRepository
import dod.game.gameobject.scheduler.SchedulerTransformer
import dod.game.model.Durations.Duration
import dod.game.model.Timer
import dod.service.event.EventService.*

import java.util.UUID

private[event] final class SchedulerEventService {

    private[event] def processSchedulerEvent(schedulerEvent: SchedulerEvent)(using gameObjectRepository: GameObjectRepository): EventResponse = schedulerEvent match {
        case SchedulerEvent.CheckScheduler(gameObjectId, schedulerName) => (gameObjectId, schedulerName) ~> {
            (gameObjectId, schedulerName) => handleCheckScheduler(gameObjectId, schedulerName)
        }

        case SchedulerEvent.ScheduleOnce(gameObjectId, schedulerName, timerId, timerKey, delay, events) => (gameObjectId, schedulerName, timerId, timerKey, delay) ~> {
            (gameObjectId, schedulerName, timerId, timerKey, delay) => handleScheduleOnce(gameObjectId, schedulerName, timerId, timerKey, delay, events)
        }

        case SchedulerEvent.ScheduleAtFixedRate(gameObjectId, schedulerName, timerId, timerKey, delay, events) => (gameObjectId, schedulerName, timerId, timerKey, delay) ~> {
            (gameObjectId, schedulerName, timerId, timerKey, delay) => handleScheduleAtFixedRate(gameObjectId, schedulerName, timerId, timerKey, delay, events)
        }

        case SchedulerEvent.RemoveScheduler(gameObjectId, schedulerName) => (gameObjectId, schedulerName) ~> {
            (gameObjectId, schedulerName) => handleSchedulerUpdate(gameObjectId, SchedulerTransformer.removeScheduler(schedulerName))
        }

        case SchedulerEvent.DelayScheduler(gameObjectId, schedulerName, duration) => (gameObjectId, schedulerName, duration) ~> {
            (gameObjectId, schedulerName, duration) => handleSchedulerUpdate(gameObjectId, SchedulerTransformer.delaySchedulerBy(schedulerName, duration))
        }
    }

    private inline def handleCheckScheduler(gameObjectId: String, schedulerName: String)(using gameObjectRepository: GameObjectRepository): EventResponse = {
        for
            scheduler <- gameObjectRepository.findScheduler(gameObjectId, schedulerName)
            timer <- gameObjectRepository.findTimer(scheduler.timerId, scheduler.timerKey)
            ready = timer.durationSince(scheduler.initialTimeStamp) >= scheduler.delay
        yield
            val events =
                if ready && scheduler.repeating then
                    SchedulerEvent.DelayScheduler(Expr(gameObjectId), Expr(schedulerName), Expr(scheduler.delay)) +: SchedulerEvent.CheckScheduler(Expr(gameObjectId), Expr(schedulerName)) +: scheduler.events
                else if ready && !scheduler.repeating then
                    SchedulerEvent.RemoveScheduler(Expr(gameObjectId), Expr(schedulerName)) +: scheduler.events
                else
                    Seq(SchedulerEvent.CheckScheduler(Expr(gameObjectId), Expr(schedulerName)))

            (gameObjectRepository, events)

    }.getOrElse {
        defaultResponse
    }

    private inline def handleScheduleOnce(gameObjectId: String, schedulerName: String, timerId: String, timerKey: String, delay: Duration, events: Seq[Event])(using gameObjectRepository: GameObjectRepository): EventResponse = {
        for
            timer <- gameObjectRepository.findTimer(timerId, timerKey)
        yield
            val schedulerTransformer = SchedulerTransformer.scheduleOnce(schedulerName, timerId, timerKey, timer.timestamp, delay, events)
            val responseEvents = Seq(SchedulerEvent.CheckScheduler(Expr(gameObjectId), Expr(schedulerName)))

            handleSchedulerUpdate(gameObjectId, schedulerTransformer, responseEvents)

    }.getOrElse {
        defaultResponse
    }

    private inline def handleScheduleAtFixedRate(gameObjectId: String, schedulerName: String, timerId: String, timerKey: String, delay: Duration, events: Seq[Event])(using gameObjectRepository: GameObjectRepository): EventResponse = {
        for
            timer <- gameObjectRepository.findTimer(timerId, timerKey)
        yield
            val schedulerTransformer = SchedulerTransformer.scheduleAtFixedRate(schedulerName, timerId, timerKey, timer.timestamp, delay, events)
            val responseEvents = Seq(SchedulerEvent.CheckScheduler(Expr(gameObjectId), Expr(schedulerName)))

            handleSchedulerUpdate(gameObjectId, schedulerTransformer, responseEvents)

    }.getOrElse {
        defaultResponse
    }

    private inline def handleSchedulerUpdate(gameObjectId: String, schedulerTransformer: SchedulerTransformer, events: Seq[Event] = Seq.empty)(using gameObjectRepository: GameObjectRepository): EventResponse =
        gameObjectRepository.findById(gameObjectId).fold(defaultResponse) { gameObject =>
            (gameObjectRepository - gameObject + gameObject.updateSchedulers(schedulerTransformer), events)
        }

}
