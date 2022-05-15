package dod.game.model

final case class Timestamp2(milliseconds: Long) extends Ordered[Timestamp2] {
    override def compare(that: Timestamp2): Int = java.lang.Long.compare(milliseconds, that.milliseconds)

    def +(duration: Duration2): Timestamp2 = Timestamp2(milliseconds + duration.milliseconds)

    def -(duration: Duration2): Timestamp2 = this + -duration
}

object Timestamp2 {

    given Ordering[Timestamp2] with {
        override def compare(x: Timestamp2, y: Timestamp2): Int = x.compare(y)
    }

    def zero: Timestamp2 = Timestamp2(0)

    def now: Timestamp2 = Timestamp2(System.currentTimeMillis())
}