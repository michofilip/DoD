package dod.service.event

import dod.game.GameStage
import dod.game.event.{Event, SchedulerEvent}
import dod.game.expression.Expr
import dod.game.gameobject.GameObjectRepository
import dod.game.gameobject.scheduler.SchedulerTransformer
import dod.game.model.Durations.Duration
import dod.game.model.Timer
import dod.service.event.EventService.*

import java.util.UUID
import scala.collection.immutable.Queue

private[event] final class SchedulerEventService {

    private[event] def processSchedulerEvent(schedulerEvent: SchedulerEvent)(using gameStage: GameStage): EventResponse = schedulerEvent match {
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

    private inline def handleCheckScheduler(gameObjectId: String, schedulerName: String)(using gameStage: GameStage): EventResponse = {
        for
            scheduler <- gameStage.gameObjects.findScheduler(gameObjectId, schedulerName)
            timer <- gameStage.gameObjects.findTimer(scheduler.timerId, scheduler.timerKey)
            ready = timer.durationSince(scheduler.initialTimeStamp) >= scheduler.delay
        yield
            val events =
                if ready && scheduler.repeating then
                    scheduler.events :+ SchedulerEvent.DelayScheduler(Expr(gameObjectId), Expr(schedulerName), Expr(scheduler.delay)) :+ SchedulerEvent.CheckScheduler(Expr(gameObjectId), Expr(schedulerName))
                else if ready && !scheduler.repeating then
                    scheduler.events :+ SchedulerEvent.RemoveScheduler(Expr(gameObjectId), Expr(schedulerName))
                else
                    Queue(SchedulerEvent.CheckScheduler(Expr(gameObjectId), Expr(schedulerName)))

            (gameStage, events)

    }.getOrElse {
        defaultResponse
    }

    private inline def handleScheduleOnce(gameObjectId: String, schedulerName: String, timerId: String, timerKey: String, delay: Duration, events: Queue[Event])(using gameStage: GameStage): EventResponse = {
        for
            timer <- gameStage.gameObjects.findTimer(timerId, timerKey)
        yield
            val schedulerTransformer = SchedulerTransformer.scheduleOnce(schedulerName, timerId, timerKey, timer.timestamp, delay, events)
            val responseEvents = Queue(SchedulerEvent.CheckScheduler(Expr(gameObjectId), Expr(schedulerName)))

            handleSchedulerUpdate(gameObjectId, schedulerTransformer, responseEvents)

    }.getOrElse {
        defaultResponse
    }

    private inline def handleScheduleAtFixedRate(gameObjectId: String, schedulerName: String, timerId: String, timerKey: String, delay: Duration, events: Queue[Event])(using gameStage: GameStage): EventResponse = {
        for
            timer <- gameStage.gameObjects.findTimer(timerId, timerKey)
        yield
            val schedulerTransformer = SchedulerTransformer.scheduleAtFixedRate(schedulerName, timerId, timerKey, timer.timestamp, delay, events)
            val responseEvents = Queue(SchedulerEvent.CheckScheduler(Expr(gameObjectId), Expr(schedulerName)))

            handleSchedulerUpdate(gameObjectId, schedulerTransformer, responseEvents)

    }.getOrElse {
        defaultResponse
    }

    private inline def handleSchedulerUpdate(gameObjectId: String, schedulerTransformer: SchedulerTransformer, events: Queue[Event] = Queue.empty)(using gameStage: GameStage): EventResponse =
        gameStage.gameObjects.findById(gameObjectId).fold(defaultResponse) { gameObject =>
            (gameStage.updateGameObjects(gameStage.gameObjects - gameObject + gameObject.updateSchedulers(schedulerTransformer)), events)
        }

}
