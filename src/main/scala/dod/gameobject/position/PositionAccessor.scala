package dod.gameobject.position

import dod.temporal.Timestamps.Timestamp

trait PositionAccessor {
    def coordinates: Option[Coordinates]

    def direction: Option[Direction]

    def positionTimestamp: Option[Timestamp]
}
