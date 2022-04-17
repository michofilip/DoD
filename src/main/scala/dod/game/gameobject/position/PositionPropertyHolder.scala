package dod.game.gameobject.position

import dod.game.gameobject.GameObject
import dod.game.gameobject.commons.CommonsPropertyHolder
import dod.game.model.Timestamps.Timestamp
import dod.game.model.{Coordinates, Direction}

private[gameobject] trait PositionPropertyHolder {
    self: GameObject =>

    protected val positionProperty: Option[PositionProperty]

    final val position = new PositionAccessor {
        override def coordinates: Option[Coordinates] = self.positionProperty.map(_.position.coordinates)

        override def direction: Option[Direction] = self.positionProperty.flatMap(_.position.direction)

        override def positionTimestamp: Option[Timestamp] = self.positionProperty.map(_.positionTimestamp)
    }

    final def updatePosition(positionTransformer: PositionTransformer, timestamp: Timestamp): GameObject =
        update(positionProperty = self.positionProperty.map(_.updatePosition(positionTransformer, timestamp)))

    final def withPositionProperty(positionProperty: PositionProperty): GameObject =
        if (self.positionProperty.isEmpty)
            update(positionProperty = Some(positionProperty))
        else this

}
