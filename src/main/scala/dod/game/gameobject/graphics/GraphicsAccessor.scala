package dod.game.gameobject.graphics

import dod.game.model.{Duration, Frame, Timestamp}

trait GraphicsAccessor {
    def layer: Option[Int]

    def length: Option[Duration]

    def frame(timestamp: Timestamp): Option[Frame]
}
