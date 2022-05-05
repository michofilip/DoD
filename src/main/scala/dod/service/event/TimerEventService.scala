package dod.service.event

import dod.game.GameStage
import dod.game.event.{Event, TimerEvent}
import dod.game.gameobject.GameObjectRepository
import dod.game.gameobject.scheduler.SchedulerTransformer
import dod.game.gameobject.timer.TimersTransformer
import dod.service.event.EventService.*

import java.util.UUID
import scala.collection.immutable.Queue

private[event] final class TimerEventService {

    private[event] def processTimerEvent(timerEvent: TimerEvent)(using gameStage: GameStage): EventResponse = timerEvent match {
        case TimerEvent.AddTimer(gameObjectId, timerName, initialTimestamp) => (gameObjectId, timerName, initialTimestamp) ~> {
            (gameObjectId, timerName, initialTimestamp) => handleTimerUpdate(gameObjectId, TimersTransformer.addTimer(timerName, initialTimestamp))
        }

        case TimerEvent.AddTimerAndStart(gameObjectId, timerName, initialTimestamp) => (gameObjectId, timerName, initialTimestamp) ~> {
            (gameObjectId, timerName, initialTimestamp) => handleTimerUpdate(gameObjectId, TimersTransformer.addTimerAndStart(timerName, initialTimestamp))
        }

        case TimerEvent.RemoveTimer(gameObjectId, timerName) => (gameObjectId, timerName) ~> {
            (gameObjectId, timerName) => handleTimerUpdate(gameObjectId, TimersTransformer.removeTimer(timerName))
        }

        case TimerEvent.RemoveAllTimers(gameObjectId) => gameObjectId ~> {
            gameObjectId => handleTimerUpdate(gameObjectId, TimersTransformer.removeAllTimers)
        }

        case TimerEvent.StartTimer(gameObjectId, timerName) => (gameObjectId, timerName) ~> {
            (gameObjectId, timerName) => handleTimerUpdate(gameObjectId, TimersTransformer.startTimer(timerName))
        }

        case TimerEvent.StopTimer(gameObjectId, timerName) => (gameObjectId, timerName) ~> {
            (gameObjectId, timerName) => handleTimerUpdate(gameObjectId, TimersTransformer.stopTimer(timerName))
        }

        case TimerEvent.StartAllTimers(gameObjectId) => gameObjectId ~> {
            gameObjectId => handleTimerUpdate(gameObjectId, TimersTransformer.startAllTimers)
        }

        case TimerEvent.StopAllTimers(gameObjectId) => gameObjectId ~> {
            gameObjectId => handleTimerUpdate(gameObjectId, TimersTransformer.stopAllTimers)
        }
    }

    private inline def handleTimerUpdate(gameObjectId: String, timerTransformer: TimersTransformer)(using gameStage: GameStage): EventResponse =
        gameStage.gameObjects.findById(gameObjectId).fold(defaultResponse) { gameObject =>
            (gameStage.updateGameObjects(gameStage.gameObjects - gameObject + gameObject.updateTimers(timerTransformer)), Queue.empty)
        }

}
