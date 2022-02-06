package dod.gameobject.position

import dod.gameobject.GameObject
import dod.gameobject.commons.CommonsPropertyHolder
import dod.temporal.Timestamps.Timestamp

private[gameobject] trait PositionPropertyHolder {
    self: GameObject =>

    protected val positionProperty: Option[PositionProperty]

    final val positionData = new PositionData {
        override def coordinates: Option[Coordinates] = self.positionProperty.map(_.coordinates)

        override def direction: Option[Direction] = self.positionProperty.map(_.direction)

        override def positionTimestamp: Option[Timestamp] = self.positionProperty.map(_.positionTimestamp)
    }

    final def updatePosition(positionTransformer: PositionTransformer, timestamp: Timestamp): GameObject =
        update(positionProperty = self.positionProperty.map(_.updatePosition(positionTransformer, timestamp)))

}
