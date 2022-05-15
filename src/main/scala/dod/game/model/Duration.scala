package dod.game.model

final case class Duration(milliseconds: Long) extends Ordered[Duration] {
    override def compare(that: Duration): Int = java.lang.Long.compare(milliseconds, that.milliseconds)

    def unary_+ : Duration = this

    def unary_- : Duration = Duration(-milliseconds)

    def +(that: Duration): Duration = Duration(milliseconds + that.milliseconds)

    def -(that: Duration): Duration = this + -that

}

object Duration {

    given Ordering[Duration] with {
        override def compare(x: Duration, y: Duration): Int = x.compare(y)
    }

    extension (value: Long) {
        def milliseconds: Duration = Duration(value)
    }

    extension (value: Double) {
        def milliseconds: Duration = Duration(value.toLong)
    }

    def zero: Duration = Duration(0)

    def between(from: Timestamp, to: Timestamp): Duration = Duration(to.milliseconds - from.milliseconds)

}
