package dod.game.model

final case class Timestamp(milliseconds: Long) extends Ordered[Timestamp] {
    override def compare(that: Timestamp): Int = java.lang.Long.compare(milliseconds, that.milliseconds)

    def +(duration: Duration): Timestamp = Timestamp(milliseconds + duration.milliseconds)

    def -(duration: Duration): Timestamp = this + -duration
}

object Timestamp {

    given Ordering[Timestamp] with {
        override def compare(x: Timestamp, y: Timestamp): Int = x.compare(y)
    }

    def zero: Timestamp = Timestamp(0)

    def now: Timestamp = Timestamp(System.currentTimeMillis())
}