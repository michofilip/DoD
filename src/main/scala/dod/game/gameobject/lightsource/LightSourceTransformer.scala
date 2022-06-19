package dod.game.gameobject.lightsource

import dod.game.model.LightSource

trait LightSourceTransformer extends (Vector[LightSource] => Vector[LightSource])

object LightSourceTransformer {

    def enable(id: Int): LightSourceTransformer = lightSources =>
        lightSources.lift(id).filterNot(_.isEnabled).fold(lightSources) { lightSource =>
            lightSources.updated(id, lightSource.enabled)
        }

    def enableAll: LightSourceTransformer = lightSources => lightSources.map(_.enabled)

    def disable(id: Int): LightSourceTransformer = lightSources =>
        lightSources.lift(id).filter(_.isEnabled).fold(lightSources) { lightSource =>
            lightSources.updated(id, lightSource.disabled)
        }

    def disableAll: LightSourceTransformer = lightSources => lightSources.map(_.disabled)

}
