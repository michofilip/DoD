package dod.game.gameobject.timer

import dod.game.model.Timer
import dod.game.model.Timestamps.Timestamp

trait TimersTransformer extends (Map[String, Timer] => Map[String, Timer])

object TimersTransformer {

    def addTimer(timerName: String, initialTimestamp: Timestamp): TimersTransformer = timers => timers + (timerName -> Timer(initialTimestamp))

    def addTimerAndStart(timerName: String, initialTimestamp: Timestamp): TimersTransformer = timers => (addTimer(timerName, initialTimestamp) andThen startTimer(timerName)) (timers)

    def removeTimer(timerName: String): TimersTransformer = timers => timers - timerName

    def removeAllTimers: TimersTransformer = _ => Map.empty

    def startTimer(timerName: String): TimersTransformer = timers => timers.get(timerName) match {
        case Some(timer) => timers + (timerName -> timer.started)
        case None => timers
    }

    def stopTimer(timerName: String): TimersTransformer = timers => timers.get(timerName) match {
        case Some(timer) => timers + (timerName -> timer.stopped)
        case None => timers
    }

    def startAllTimers: TimersTransformer = timers => timers.transform { case (_, timer) => timer.started }

    def stopAllTimers: TimersTransformer = timers => timers.transform { case (_, timer) => timer.stopped }

    // TODO 
    // add to timer
    // subtract from timer
}