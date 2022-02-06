package dod.gameobject.position

import dod.temporal.Timestamps.Timestamp

import scala.util.chaining.scalaUtilChainingOps

final class PositionProperty(private[position] val coordinates: Coordinates,
                             private[position] val direction: Direction,
                             private[position] val positionTimestamp: Timestamp) {

    def updatePosition(positionTransformer: PositionTransformer, timestamp: Timestamp): PositionProperty =
        positionTransformer(coordinates, direction) match {
            case (coordinates, direction) if coordinates != this.coordinates || direction != this.direction =>
                new PositionProperty(coordinates, direction, timestamp)

            case _ => this
        }

}
