package dod.utils

object MathUtils {
    def mod(x: Int, n: Int): Int =
        if x < 0 then x % n + n else x % n

    def bound(x: Int, min: Int, max: Int): Int =
        if x < min then min else if x < max then x else max

    def ceil(x: Double): Int =
        Math.ceil(x).toInt

    def floor(x: Double): Int =
        Math.floor(x).toInt
//
//    def round(x: Double, prec: Int): Double =
//        val tens = Math.pow(10, prec.toDouble)
//        Math.round(tens * x) / tens
//
//    extension (x: Int) {
//        def %%(n: Int): Int =
//            mod(x, n)
//
//        def ><(min: Int, max: Int): Int =
//            bound(x, min, max)
//    }
}
