package dod

import dod.data.SpriteRepository

import scala.util.Random

object Main1 {

    @main
    def run(): Unit = {

        val precision = 8
        val width = 200
        val height = 30

        val rand = new Random()

        def coefficients = Seq.tabulate(precision + 1, precision + 1)((i, j) => (i, j) -> (2 * rand.nextDouble() - 1)).flatten.toMap

        def coefficients1 = Seq.tabulate(precision + 1, precision + 1)((i, j) => (i, j) -> 1).flatten.toMap

        val a = coefficients
        val b = coefficients
        val c = coefficients
        val d = coefficients

        def f(x: Double, y: Double): Double = {
            for (i <- 0 to precision; j <- 0 to precision) yield {
                //                a(i, j) * Math.cos(i * x) * Math.cos(j * y)
                //                a(i, j) * Math.sin(i * x) * Math.sin(j * y)

                a(i, j) * Math.sin(i * x) * Math.sin(j * y)
                    + b(i, j) * Math.sin(i * x) * Math.cos(j * y)
                    + c(i, j) * Math.cos(i * x) * Math.sin(j * y)
                    + d(i, j) * Math.cos(i * x) * Math.cos(j * y)
            }
        }.sum

        val max = (for (x <- 0 to width; y <- 0 to height) yield f(x, y)).max

        def f1(x: Int, y: Int): Double = {
            f((x + .5) * 2 * Math.PI / width, (y + .5) * 1 * Math.PI / height) / max
        }

        for (y <- 0 to height) {
            for (x <- 0 to width) {
                print(if f1(x, y) >= 0.25 then "X" else " ")
            }
            println()
        }

    }
}
