package dod.game.model

import dod.game.model.{Coordinates, Direction, Position}

case class Position(coordinates: Coordinates, direction: Option[Direction])

object Position {
    def apply(coordinates: Coordinates): Position = {
        new Position(coordinates = coordinates, direction = None)
    }

    def apply(coordinates: Coordinates, direction: Direction): Position = {
        new Position(coordinates = coordinates, direction = Some(direction))
    }
}
