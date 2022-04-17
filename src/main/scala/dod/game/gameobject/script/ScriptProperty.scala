package dod.game.gameobject.script

import dod.game.gameobject.scheduler.{SchedulerProperty, SchedulerTransformer}
import dod.game.model.{Scheduler, Script}

final class ScriptProperty(private[script] val scripts: Map[String, Script] = Map.empty) {

    def updateScripts(scriptTransformer: ScriptTransformer): ScriptProperty = {
        new ScriptProperty(scriptTransformer(scripts))
    }

}
