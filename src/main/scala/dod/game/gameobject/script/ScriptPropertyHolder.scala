package dod.game.gameobject.script

import dod.game.event.Event
import dod.game.gameobject.GameObject
import dod.game.gameobject.scheduler.{SchedulerProperty, SchedulerTransformer}
import dod.game.model.Durations.Duration
import dod.game.model.{Scheduler, Script}

import java.util.UUID

trait ScriptPropertyHolder {
    self: GameObject =>

    protected val scriptProperty: Option[ScriptProperty]

    final def script(scriptName: String): Option[Script] = self.scriptProperty.flatMap(_.scripts.get(scriptName))

    final def updateScripts(scriptTransformer: ScriptTransformer): GameObject =
        update(scriptProperty = self.scriptProperty.map(_.updateScripts(scriptTransformer)))

}
