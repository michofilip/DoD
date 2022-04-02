package dod.data

import dod.game.model.Frame

class FrameRepository {
    case class FrameData(id: Int, spriteId: Int, offsetX: Double, offsetY: Double)

    private val frameById = Seq(
        FrameData(1, 3, 0, 0),
        FrameData(2, 4, 0, 0),
        FrameData(3, 5, 0, 0),
        FrameData(4, 6, 0, 0),
        FrameData(5, 7, 0, 0),
        FrameData(6, 8, 0, 0),
        FrameData(7, 9, 0, 0),
        FrameData(8, 10, 0, 0),
        FrameData(9, 11, 0, 0),
        FrameData(10, 12, 0, 0)
    ).map { frameData =>
        frameData.id -> frameFrom(frameData)
    }.toMap

    inline private def frameFrom(frameData: FrameData): Frame = {
        Frame(frameData.spriteId, frameData.offsetX, frameData.offsetY)
    }

    def findById(id: Int): Frame = frameById(id)
}
