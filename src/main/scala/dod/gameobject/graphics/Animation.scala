package dod.gameobject.graphics

import dod.temporal.Durations.*
import dod.utils.MathUtils

class Animation(val fps: Double, val looped: Boolean, private val frames: IndexedSeq[Frame]) {
    def length: Duration = (frames.length / fps * 1000).milliseconds

    def frame(duration: Duration): Frame = {
        val frameNo = MathUtils.floor(duration.value * fps / 1000)
        val frameIndex = if (looped) {
            MathUtils.mod(frameNo, frames.length)
        } else {
            MathUtils.bound(frameNo, 0, frames.length - 1)
        }
        frames(frameIndex)
    }
}
