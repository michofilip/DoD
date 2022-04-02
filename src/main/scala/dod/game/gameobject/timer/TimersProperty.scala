package dod.game.gameobject.timer

import dod.game.model.Timer

final class TimersProperty(private[timer] val timers: Map[String, Timer] = Map.empty) {

    def updateTimers(timersTransformer: TimersTransformer): TimersProperty = {
        new TimersProperty(timersTransformer(timers))
    }

}
