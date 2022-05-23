package dod

import dod.ShadowMain.ShadowService.{*, given}
import dod.game.model.{Coordinates, Shift}

import java.util.Comparator
import scala.annotation.tailrec
import scala.util.chaining.scalaUtilChainingOps

object ShadowMain {

    class ShadowService(range: Int) {

        private val shifts: Seq[Shift] = {
            val offset = Math.sqrt(2) - 1

            for {
                x <- -range to range
                y <- -range to range
                s = Shift(x, y)
                if length(s) <= range + offset
            } yield s
        }.sorted.toList

        private val angles = shifts.flatMap(anglesFrom).distinct.toList

        private val shiftToShadow: Map[Shift, List[Double]] = {
            def shadow(shift: Shift): List[Double] = {
                val x = shift.dx
                val y = shift.dy

                if (x > 0 && y > 0) {
                    val min = angle(x - .5, y + .5)
                    val max = angle(x + .5, y - .5)
                    angles.filter(a => min < a && a < max)
                } else if (x > 0 && y < 0) {
                    val min = angle(x + .5, y + .5)
                    val max = angle(x - .5, y - .5)
                    angles.filter(a => min < a && a < max)
                } else if (x < 0 && y > 0) {
                    val min = angle(x - .5, y - .5)
                    val max = angle(x + .5, y + .5)
                    angles.filter(a => min < a && a < max)
                } else if (x < 0 && y < 0) {
                    val min = angle(x + .5, y - .5)
                    val max = angle(x - .5, y + .5)
                    angles.filter(a => min < a && a < max)
                } else if (x > 0) {
                    val min = angle(x - .5, .5)
                    val max = angle(x - .5, -.5)
                    angles.filter(a => min < a && a < max)
                } else if (x < 0) {
                    val min = angle(x + .5, -.5)
                    val max = angle(x + .5, .5)
                    angles.filter(a => min < a && a < max)
                } else if (y > 0) {
                    val min = angle(-.5, y - .5)
                    val max = angle(.5, y - .5)
                    angles.filter(a => min < a || a < max)
                } else if (y < 0) {
                    val min = angle(.5, y + .5)
                    val max = angle(-.5, y + .5)
                    angles.filter(a => min < a && a < max)
                } else {
                    angles
                }
            }

            shifts.map(s => s -> shadow(s)).toMap
        }

        def lit(focus: Coordinates, dir: Double, angularWidth: Double, opaques: Set[Coordinates]): Set[Coordinates] = {
            @tailrec
            def l(shifts: Seq[Shift], alreadyLit: Set[Coordinates], shadows: Set[Double]): Set[Coordinates] = {
                shifts match
                    case s +: rest =>
                        val sh = shiftToShadow(s)
                        val c = focus.moveBy(s)

                        val newAlreadyLit = if (!alreadyLit.contains(c) && sh.exists(sh => !shadows.contains(sh))) {
                            alreadyLit + c
                        } else {
                            alreadyLit
                        }

                        val newOpaques = if (opaques.contains(c)) {
                            shadows ++ sh
                        } else {
                            shadows
                        }

                        l(rest, newAlreadyLit, newOpaques)
                    case _ => alreadyLit
            }

            l(shifts, Set.empty, Set.empty)
        }
    }

    object ShadowService {

        private def angle(x: Double, y: Double): Double = {
            Math.atan2(x, y).pipe { theta =>
                if theta < 0 then 2 * Math.PI + theta else theta
            }
        }

        private def anglesFrom(shift: Shift): Seq[Double] = Seq(
            angle(shift.dx - .5, shift.dy - .5),
            angle(shift.dx - .5, shift.dy + .5),
            angle(shift.dx + .5, shift.dy - .5),
            angle(shift.dx + .5, shift.dy + .5)
        )

        private def length(shift: Shift): Double =
            Math.sqrt(shift.dx * shift.dx + shift.dy * shift.dy)

        given Ordering[Shift] with {
            override def compare(x: Shift, y: Shift): Int = java.lang.Double.compare(length(x), length(y))
        }

    }

    def main(args: Array[String]): Unit = {
        case class Element(coordinates: Coordinates, char: Char, opaque: Boolean)
        case class Cone(coordinates: Coordinates, dir: Double, angularWidth: Double)

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

        val lit = shadowService.lit(Coordinates(0, 0), 0, Math.PI / 2, opaques)

        for (y <- -r to r) {
            for (x <- -r to r) {
                val c = Coordinates(x, y)
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
