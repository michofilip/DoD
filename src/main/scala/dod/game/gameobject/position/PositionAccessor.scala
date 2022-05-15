package dod.game.gameobject.position

import dod.game.model.{Coordinates, Direction, Timestamp}

trait PositionAccessor {
    def coordinates: Option[Coordinates]

    def direction: Option[Direction]

    def positionTimestamp: Option[Timestamp]
}
