package dod.game.gameobject.position

import dod.game.model.{Position, Timestamp}

import scala.util.chaining.scalaUtilChainingOps

final class PositionProperty(private[position] val position: Position,
                             private[position] val positionTimestamp: Timestamp) {

    def updatePosition(positionTransformer: PositionTransformer, timestamp: Timestamp): PositionProperty =
        positionTransformer(position) match {
            case position if position != this.position =>
                new PositionProperty(position, timestamp)

            case _ => this
        }

}
