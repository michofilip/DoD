package dod

import dod.ShadowMain.ShadowService.{*, given}
import dod.game.model.{Coordinates, Shift}

import java.util.Comparator
import scala.annotation.tailrec
import scala.util.chaining.scalaUtilChainingOps

object ShadowMain {

    case class LightSource(origin: Coordinates, direction: Double, angularWidth: Double, range: Int)

    class ShadowService(maxRange: Int) {
        private val offset = Math.sqrt(2) - 1

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

                if (x > 0 && y > 0) {
                    val min = getAngle(x - .5, y + .5)
                    val max = getAngle(x + .5, y - .5)
                    angles.filter(a => min < a && a < max)
                } else if (x > 0 && y < 0) {
                    val min = getAngle(x + .5, y + .5)
                    val max = getAngle(x - .5, y - .5)
                    angles.filter(a => min < a && a < max)
                } else if (x < 0 && y > 0) {
                    val min = getAngle(x - .5, y - .5)
                    val max = getAngle(x + .5, y + .5)
                    angles.filter(a => min < a && a < max)
                } else if (x < 0 && y < 0) {
                    val min = getAngle(x + .5, y - .5)
                    val max = getAngle(x - .5, y + .5)
                    angles.filter(a => min < a && a < max)
                } else if (x > 0) {
                    val min = getAngle(x - .5, .5)
                    val max = getAngle(x - .5, -.5)
                    angles.filter(a => min < a && a < max)
                } else if (x < 0) {
                    val min = getAngle(x + .5, -.5)
                    val max = getAngle(x + .5, .5)
                    angles.filter(a => min < a && a < max)
                } else if (y > 0) {
                    val min = getAngle(-.5, y - .5)
                    val max = getAngle(.5, y - .5)
                    angles.filter(a => min < a || a < max)
                } else if (y < 0) {
                    val min = getAngle(.5, y + .5)
                    val max = getAngle(-.5, y + .5)
                    angles.filter(a => min < a && a < max)
                } else {
                    angles
                }
            }

            shifts.map(shift => shift -> shadow(shift)).toMap
        }

        def getLitCoordinates(lightSources: Seq[LightSource], opaques: Set[Coordinates]): Set[Coordinates] = {
            def initialCone(lightSource: LightSource) = {
                inline val twoPi = 2 * Math.PI

                if (lightSource.angularWidth >= twoPi) {
                    Set.empty
                } else {
                    val alpha = lightSource.direction - lightSource.angularWidth / 2
                    val beta = lightSource.direction + lightSource.angularWidth / 2

                    if (0 <= alpha && beta <= twoPi) {
                        angles.filterNot(a => alpha <= a && a <= beta).toSet
                    } else if (alpha < 0) {
                        angles.filterNot(a => alpha + twoPi <= a || a <= beta).toSet
                    } else if (beta > twoPi) {
                        angles.filterNot(a => alpha <= a || a <= beta - twoPi).toSet
                    } else {
                        Set.empty
                    }
                }
            }

            @tailrec
            def l(shifts: Seq[Shift], lightSource: LightSource, litCoordinates: Set[Coordinates], shadows: Set[Double]): Set[Coordinates] = {
                shifts match
                    case shift +: rest =>
                        val shadow = shiftToShadow(shift)
                        val coordinates = lightSource.origin.moveBy(shift)
                        val inRange = shift.distanceToZero <= lightSource.range + offset

                        val isLit = !litCoordinates.contains(coordinates) && shadow.exists(sh => !shadows.contains(sh)) && inRange
                        val castShadow = isLit && (opaques.contains(coordinates) || !inRange)

                        val litCoordinatesUpdated = if (isLit) litCoordinates + coordinates else litCoordinates
                        val shadowsUpdated = if (castShadow) shadows ++ shadow else shadows

                        l(rest, lightSource, litCoordinatesUpdated, shadowsUpdated)
                    case _ => litCoordinates
            }

            lightSources.foldLeft(Set.empty) { case (litCoordinates, lightSource) =>
                l(shifts, lightSource, litCoordinates, initialCone(lightSource))
            }
        }
    }

    object ShadowService {

        private def getAngle(x: Double, y: Double): Double = {
            Math.atan2(x, y).pipe { theta =>
                if theta < 0 then 2 * Math.PI + theta else theta
            }
        }

        private def anglesFrom(shift: Shift): Seq[Double] = Seq(
            getAngle(shift.dx - .5, shift.dy - .5),
            getAngle(shift.dx - .5, shift.dy + .5),
            getAngle(shift.dx + .5, shift.dy - .5),
            getAngle(shift.dx + .5, shift.dy + .5)
        )

        extension (shift: Shift) {
            def distanceToZero: Double = Math.sqrt(shift.dx * shift.dx + shift.dy * shift.dy)
        }

        given Ordering[Shift] with {
            override def compare(x: Shift, y: Shift): Int = java.lang.Double.compare(x.distanceToZero, y.distanceToZero)
        }

    }

    def main(args: Array[String]): Unit = {
        case class Element(coordinates: Coordinates, char: Char, opaque: Boolean)

        val r = 10

        val shadowService = new ShadowService(r)

        val elements = Seq(
            Element(Coordinates(0, 0), '@', false),
            Element(Coordinates(2, 0), '#', true),
            Element(Coordinates(-2, 0), '#', true),
            Element(Coordinates(0, 2), '#', true),
            Element(Coordinates(0, -2), '#', true),
            Element(Coordinates(2, 2), '#', true),
            Element(Coordinates(2, -2), '#', true),
            Element(Coordinates(-2, 2), '#', true),
            Element(Coordinates(-2, -2), '#', true),
        )

        val opaques = elements.filter(_.opaque).map(_.coordinates).toSet
        val chars = elements.map(e => e.coordinates -> e.char).toMap

        val lit = shadowService.getLitCoordinates(
            Seq(
                LightSource(Coordinates(0, 0), 0 * 2 * Math.PI, 2 * Math.PI, 10),
                //                LightSource(Coordinates(8, 8),.625 * 2 * Math.PI,.25 * 2 * Math.PI, 10),
                //                LightSource(Coordinates(-8, -8),.125 * 2 * Math.PI,.25 * 2 * Math.PI, 10)
            ),
            opaques
        )

        for (y <- -r to r) {
            for (x <- -r to r) {
                val c = Coordinates(x, -y)
                if (lit.contains(c)) {
                    print(chars.getOrElse(c, ' '))
                } else {
                    print('X')
                }
                print(' ')
            }
            println()
        }

    }
}
