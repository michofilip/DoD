package dod.service.event

import dod.game.event.{Event, TimerEvent}
import dod.game.gameobject.GameObjectRepository
import dod.game.gameobject.scheduler.SchedulerTransformer
import dod.game.gameobject.timer.TimersTransformer
import dod.service.event.EventService.EventResponse

import java.util.UUID

private[event] final class TimerEventService {

    def processTimerEvent(gameObjectRepository: GameObjectRepository, timerEvent: TimerEvent): EventResponse = timerEvent match {
        case TimerEvent.AddTimer(gameObjectId, timerName, initialTimestamp) =>
            handleTimerUpdate(gameObjectRepository, gameObjectId, TimersTransformer.addTimer(timerName, initialTimestamp))

        case TimerEvent.AddTimerAndStart(gameObjectId, timerName, initialTimestamp) =>
            handleTimerUpdate(gameObjectRepository, gameObjectId, TimersTransformer.addTimerAndStart(timerName, initialTimestamp))

        case TimerEvent.RemoveTimer(gameObjectId, timerName) =>
            handleTimerUpdate(gameObjectRepository, gameObjectId, TimersTransformer.removeTimer(timerName))

        case TimerEvent.RemoveAllTimers(gameObjectId) =>
            handleTimerUpdate(gameObjectRepository, gameObjectId, TimersTransformer.removeAllTimers)

        case TimerEvent.StartTimer(gameObjectId, timerName) =>
            handleTimerUpdate(gameObjectRepository, gameObjectId, TimersTransformer.startTimer(timerName))

        case TimerEvent.StopTimer(gameObjectId, timerName) =>
            handleTimerUpdate(gameObjectRepository, gameObjectId, TimersTransformer.stopTimer(timerName))

        case TimerEvent.StartAllTimers(gameObjectId) =>
            handleTimerUpdate(gameObjectRepository, gameObjectId, TimersTransformer.startAllTimers)

        case TimerEvent.StopAllTimers(gameObjectId) =>
            handleTimerUpdate(gameObjectRepository, gameObjectId, TimersTransformer.stopAllTimers)
    }

    private inline def handleTimerUpdate(gameObjectRepository: GameObjectRepository,
                                         gameObjectId: String,
                                         timerTransformer: TimersTransformer): EventResponse =
        gameObjectRepository.findById(gameObjectId).fold((gameObjectRepository, Seq.empty)) { gameObject =>
            (gameObjectRepository - gameObject + gameObject.updateTimers(timerTransformer), Seq.empty)
        }

}
