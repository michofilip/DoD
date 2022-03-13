package dod.game.gameobject.timer

import dod.game.temporal.Timer
import dod.game.temporal.Timestamps.Timestamp

trait TimersTransformer extends (Map[String, Timer] => Map[String, Timer]) {

    def addTimer(key: String, initialTimestamp: Timestamp): TimersTransformer = timers => timers + (key -> Timer(initialTimestamp))

    def removeTimer(key: String): TimersTransformer = timers => timers - key

}
