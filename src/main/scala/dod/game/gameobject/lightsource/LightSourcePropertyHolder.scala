package dod.game.gameobject.lightsource

import dod.game.gameobject.GameObject
import dod.game.gameobject.lightsource.LightSourcePropertyHolder.angleFrom
import dod.game.gameobject.position.PositionPropertyHolder
import dod.game.model.Direction.{East, North, South, West}
import dod.game.model.{Direction, LightCone}

import javax.lang.model.element.ModuleElement.Directive

private[gameobject] trait LightSourcePropertyHolder {
    self: GameObject with PositionPropertyHolder =>

    protected val lightSourceProperty: Option[LightSourceProperty]

    final def lightCones: Seq[LightCone] = {
        for {
            lightSourceProperty <- self.lightSourceProperty
            coordinates <- self.position.coordinates
            direction <- self.position.direction
        } yield for {
            lightSource <- lightSourceProperty.lightSources
            if lightSource.isEnabled
        } yield LightCone(
            origin = coordinates,
            direction = angleFrom(direction) + lightSource.directionOffset,
            angularWidth = lightSource.angularWidth,
            range = lightSource.range
        )
    }.getOrElse(Seq.empty)

    def updateLightSources(lightSourceTransformer: LightSourceTransformer): GameObject =
        update(lightSourceProperty = self.lightSourceProperty.map(_.updateLightSources(lightSourceTransformer)))

    def withLightSourceProperty(): GameObject =
        if (self.lightSourceProperty.isEmpty)
            update(lightSourceProperty = Some(LightSourceProperty()))
        else this
}

object LightSourcePropertyHolder {
    private def angleFrom(direction: Direction): Double = direction match {
        case North => 0.0
        case East => 0.5 * Math.PI
        case South => Math.PI
        case West => 1.5 * Math.PI
    }
}
