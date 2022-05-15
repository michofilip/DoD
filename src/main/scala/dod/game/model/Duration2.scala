package dod.game.model

final case class Duration2(milliseconds: Long) extends Ordered[Duration2] {
    override def compare(that: Duration2): Int = java.lang.Long.compare(milliseconds, that.milliseconds)

    def unary_+ : Duration2 = this

    def unary_- : Duration2 = Duration2(-milliseconds)

    def +(that: Duration2): Duration2 = Duration2(milliseconds + that.milliseconds)

    def -(that: Duration2): Duration2 = this + -that

}

object Duration2 {

    given Ordering[Duration2] with {
        override def compare(x: Duration2, y: Duration2): Int = x.compare(y)
    }

    def zero: Duration2 = Duration2(0)

    def between(from: Timestamp2, to: Timestamp2): Duration2 = Duration2(to.milliseconds - from.milliseconds)

}
