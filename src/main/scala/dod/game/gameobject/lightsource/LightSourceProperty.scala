package dod.game.gameobject.lightsource

import dod.game.model.LightSource

class LightSourceProperty(private[lightsource] val lightSources: Vector[LightSource] = Vector.empty) {

    def updateLightSources(lightSourceTransformer: LightSourceTransformer): LightSourceProperty = {
        new LightSourceProperty(lightSourceTransformer(lightSources))
    }

}
