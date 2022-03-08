package dod.game.gameobject.graphics

import dod.game.temporal.Durations.*
import dod.utils.MathUtils

sealed abstract class Animation {
    def layer: Int

    def length: Duration

    def frame(duration: Duration): Frame
}

object Animation {

    final class OneFrameAnimation(override val layer: Int, frame: Frame) extends Animation {
        override def length: Duration = Duration.zero

        override def frame(duration: Duration): Frame = frame
    }

    final class SingleRunAnimation(override val layer: Int, fps: Double, frames: IndexedSeq[Frame]) extends Animation {
        override def length: Duration = (frames.length / fps * 1000).milliseconds

        override def frame(duration: Duration): Frame = {
            inline def frameNo = MathUtils.floor(duration.value * fps / 1000)

            inline def frameIndex = MathUtils.bound(frameNo, 0, frames.length - 1)

            frames(frameIndex)
        }
    }

    final class LoopingAnimation(override val layer: Int, fps: Double, frames: IndexedSeq[Frame]) extends Animation {
        override def length: Duration = (frames.length / fps * 1000).milliseconds

        override def frame(duration: Duration): Frame = {
            inline def frameNo = MathUtils.floor(duration.value * fps / 1000)

            inline def frameIndex = MathUtils.mod(frameNo, frames.length)

            frames(frameIndex)
        }
    }
}
