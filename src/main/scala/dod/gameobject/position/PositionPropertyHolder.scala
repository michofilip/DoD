package dod.gameobject.position

import dod.gameobject.GameObject
import dod.temporal.Timestamps.Timestamp

private[gameobject] trait PositionPropertyHolder[T <: GameObject] {
    protected val positionProperty: Option[PositionProperty]

    final val positionData = new PositionData {
        override def coordinates: Option[Coordinates] = positionProperty.map(_.coordinates)

        override def direction: Option[Direction] = positionProperty.map(_.direction)

        override def positionTimestamp: Option[Timestamp] = positionProperty.map(_.positionTimestamp)
    }

    def updatePosition(positionTransformer: PositionTransformer, timestamp: Timestamp): T

}
