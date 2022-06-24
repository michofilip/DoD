package dod.service

import dod.game.model.{Coordinates, LightCone, Shift}
import dod.service.ShadowCastingService.{*, given}

import scala.annotation.tailrec

class ShadowCastingService(maxRange: Int) {
    private val shifts: Seq[Shift] = {
        for {
            x <- -maxRange to maxRange
            y <- -maxRange to maxRange
            shift = Shift(x, y)
            if shift.distanceToZero <= maxRange + offset
        } yield shift
    }.sorted.toList

    private val angles = shifts.flatMap(anglesFrom).distinct.toList

    private val shiftToShadow: Map[Shift, List[Double]] = {
        def shadow(shift: Shift): List[Double] = {
            val x = shift.dx
            val y = shift.dy

            val angleFilter = (angle: Double) =>
                if x > 0 && y > 0 then Math.atan2(x - .5, y + .5) < angle && angle < Math.atan2(x + .5, y - .5)
                else if x > 0 && y < 0 then Math.atan2(x + .5, y + .5) < angle && angle < Math.atan2(x - .5, y - .5)
                else if x < 0 && y > 0 then Math.atan2(x - .5, y - .5) < angle && angle < Math.atan2(x + .5, y + .5)
                else if x < 0 && y < 0 then Math.atan2(x + .5, y - .5) < angle && angle < Math.atan2(x - .5, y + .5)
                else if x > 0 then Math.atan2(x - .5, .5) < angle && angle < Math.atan2(x - .5, -.5)
                else if x < 0 then Math.atan2(x + .5, -.5) < angle && angle < Math.atan2(x + .5, .5)
                else if y > 0 then Math.atan2(-.5, y - .5) < angle && angle < Math.atan2(.5, y - .5)
                else if y < 0 then Math.atan2(.5, y + .5) < angle || angle < Math.atan2(-.5, y + .5)
                else true

            angles.filter(angleFilter)
        }

        shifts.map(shift => shift -> shadow(shift)).toMap
    }

    def getLitCoordinates(lightCones: Seq[LightCone], opaques: Set[Coordinates]): Set[Coordinates] = {
        inline def initialCone(lightCone: LightCone): Set[Double] = {
            inline val twoPi = 2 * Math.PI

            if lightCone.angularWidth >= twoPi then
                Set.empty
            else {
                val alpha = lightCone.direction - lightCone.angularWidth / 2
                val beta = lightCone.direction + lightCone.angularWidth / 2

                if -Math.PI <= alpha && beta <= Math.PI then angles.filterNot(a => alpha <= a && a <= beta).toSet
                else if alpha < -Math.PI then angles.filterNot(a => alpha + twoPi <= a || a <= beta).toSet
                else if beta > Math.PI then angles.filterNot(a => alpha <= a || a <= beta - twoPi).toSet
                else Set.empty
            }
        }

        inline def lit(litCoordinates: Set[Coordinates], lightCone: LightCone): Set[Coordinates] = {
            val origin = lightCone.origin
            val range = lightCone.range + offset

            @tailrec
            def l(shifts: Seq[Shift], litCoordinates: Set[Coordinates], shadows: Set[Double]): Set[Coordinates] = shifts match
                case shift +: rest =>
                    val shadow = shiftToShadow(shift)
                    val coordinates = origin.moveBy(shift)

                    val isLit = shift.distanceToZero <= range && !litCoordinates.contains(coordinates) && !shadow.forall(shadows.contains)
                    val castShadow = opaques.contains(coordinates)

                    val litCoordinatesUpdated = if isLit then litCoordinates + coordinates else litCoordinates
                    val shadowsUpdated = if castShadow then shadows ++ shadow else shadows

                    l(rest, litCoordinatesUpdated, shadowsUpdated)
                case _ => litCoordinates

            l(shifts, litCoordinates, initialCone(lightCone))
        }

        lightCones.foldLeft(Set.empty) { case (litCoordinates, lightCone) =>
            lit(litCoordinates, lightCone)
        }
    }
}

object ShadowCastingService {

    private val offset = Math.sqrt(2) - 1

    private def anglesFrom(shift: Shift): Seq[Double] = Seq(
        Math.atan2(shift.dx - .5, shift.dy - .5),
        Math.atan2(shift.dx - .5, shift.dy + .5),
        Math.atan2(shift.dx + .5, shift.dy - .5),
        Math.atan2(shift.dx + .5, shift.dy + .5)
    )

    extension (shift: Shift) {
        def distanceToZero: Double = Math.sqrt(shift.dx * shift.dx + shift.dy * shift.dy)
    }

    given Ordering[Shift] with {
        override def compare(x: Shift, y: Shift): Int = java.lang.Double.compare(x.distanceToZero, y.distanceToZero)
    }

}