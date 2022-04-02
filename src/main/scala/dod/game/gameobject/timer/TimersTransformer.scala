package dod.game.gameobject.timer

import dod.game.model.Timer
import dod.game.model.Timestamps.Timestamp

trait TimersTransformer extends (Map[String, Timer] => Map[String, Timer])

object TimersTransformer {

    def addTimer(key: String, initialTimestamp: Timestamp): TimersTransformer = timers => timers + (key -> Timer(initialTimestamp))

    def addTimerAndStart(key: String, initialTimestamp: Timestamp): TimersTransformer = timers => (addTimer(key, initialTimestamp) andThen startTimer(key)) (timers)

    def removeTimer(key: String): TimersTransformer = timers => timers - key

    def removeAllTimers: TimersTransformer = _ => Map.empty

    def startTimer(key: String): TimersTransformer = timers => timers.get(key) match {
        case Some(timer) => timers + (key -> timer.started)
        case None => timers
    }

    def stopTimer(key: String): TimersTransformer = timers => timers.get(key) match {
        case Some(timer) => timers + (key -> timer.stopped)
        case None => timers
    }

    def startAllTimers: TimersTransformer = timers => timers.transform { case (_, timer) => timer.started }

    def stopAllTimers: TimersTransformer = timers => timers.transform { case (_, timer) => timer.stopped }

    // TODO 
    // add to timer
    // subtract from timer
}