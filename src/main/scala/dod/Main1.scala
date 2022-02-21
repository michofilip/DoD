package dod

import dod.ui.SpriteData

import scala.util.Random

object Main1 {

    @main
    def run(): Unit = {

        val precision = 8
        val c = 1
        val width = 100
        val height = 30

        val rand = new Random()

        val coefficients = {
            for (i <- 0 to precision; j <- 0 to precision) yield {
                (i, j) -> (2 * rand.nextDouble() - 1)
            }
        }.toMap

        val coeffA = Seq.tabulate(precision + 1)(_ => (2 * rand.nextDouble() - 1))
        val coeffB = Seq.tabulate(precision + 1)(_ => (2 * rand.nextDouble() - 1))
        val coeffC = Seq.tabulate(precision + 1)(_ => (2 * rand.nextDouble() - 1))
        val coeffD = Seq.tabulate(precision + 1)(_ => (2 * rand.nextDouble() - 1))

        def f(x: Double, y: Double): Double = {
            for (i <- 0 to precision; j <- 0 to precision) yield {
                coefficients(i, j) * Math.cos(i * x) * Math.cos(j * y)
                //                coefficients(i, j) * Math.sin(i * x) * Math.sin(j * y)
                //                coeffA(i) * coeffC(j) * Math.sin(i * x) * Math.sin(j * y)
                //                    + coeffA(i) * coeffD(j) * Math.sin(i * x) * Math.cos(j * y)
                //                    + coeffB(i) * coeffC(j) * Math.cos(i * x) * Math.sin(j * y)
                //                    + coeffB(i) * coeffD(j) * Math.cos(i * x) * Math.cos(j * y)
            }
        }.sum

        def f1(x: Int, y: Int): Double = {
            c * f((x + .5) * Math.PI / width, (y + .5) * Math.PI / height)
        }

        for (y <- 0 until height) {
            for (x <- 0 until width) {
                print(if f1(x, y) > 0 then "X" else " ")
            }
            println()
        }

        //        val tiles = {
        //            for (x <- 0 until width; y <- 0 until height) yield {
        //                (x, y) -> (if f1(x, y) > 0 then "X" else " ")
        //            }
        //        }

    }
}
