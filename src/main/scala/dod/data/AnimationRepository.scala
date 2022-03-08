package dod.data

import dod.game.gameobject.graphics.{Animation, AnimationSelector, Frame}
import dod.game.gameobject.position.Direction

class AnimationRepository(frameRepository: FrameRepository) {
    case class AnimationData(id: Int, description: String, looping: Boolean, layer: Int, fps: Double, frames: Seq[Int])

    private val animationById = Seq(
        AnimationData(id = 1, description = "floor", looping = false, layer = 0, fps = 4, frames = Seq(1)),
        AnimationData(id = 2, description = "wall", looping = false, layer = 4, fps = 4, frames = Seq(2)),
        AnimationData(id = 3, description = "player_north", looping = false, layer = 3, fps = 4, frames = Seq(3)),
        AnimationData(id = 4, description = "player_east", looping = false, layer = 3, fps = 4, frames = Seq(4)),
        AnimationData(id = 5, description = "player_south", looping = false, layer = 3, fps = 4, frames = Seq(5)),
        AnimationData(id = 6, description = "player_west", looping = false, layer = 3, fps = 4, frames = Seq(6)),
        AnimationData(id = 7, description = "door_open", looping = false, layer = 1, fps = 4, frames = Seq(7)),
        AnimationData(id = 8, description = "door_closed", looping = false, layer = 4, fps = 4, frames = Seq(8)),
        AnimationData(id = 9, description = "switch_off", looping = false, layer = 2, fps = 4, frames = Seq(9)),
        AnimationData(id = 10, description = "switch_on", looping = false, layer = 2, fps = 4, frames = Seq(10))
    ).map { animationData =>
        animationData.id -> animationFrom(animationData)
    }.toMap

    inline private def animationFrom(animationData: AnimationData): Animation = {
        val frames = animationData.frames.map(frameRepository.findById).toVector

        if (frames.length == 1) {
            new Animation.OneFrameAnimation(animationData.layer, frames.head)
        } else if (animationData.looping) {
            new Animation.LoopingAnimation(animationData.layer, animationData.fps, frames)
        } else {
            new Animation.SingleRunAnimation(animationData.layer, animationData.fps, frames)
        }
    }

    def findById(id: Int): Animation = animationById(id)
}
