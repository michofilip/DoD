package dod.data

import dod.game.model.{Coordinates, Direction, Frame, Position}

class PositionRepository {
    enum PositionType {
        case OnlyCoordinates
        case CoordinatesAndDirection
    }

    case class PositionData(name: String, positionType: PositionType)

    private val positionByName = Seq(
        PositionData(name = "floor", positionType = PositionType.OnlyCoordinates),
        PositionData(name = "wall", positionType = PositionType.OnlyCoordinates),
        PositionData(name = "player", positionType = PositionType.CoordinatesAndDirection),
        PositionData(name = "door", positionType = PositionType.OnlyCoordinates),
        PositionData(name = "switch", positionType = PositionType.OnlyCoordinates)
    ).map { positionData =>
        positionData.name -> positionFrom(positionData)
    }.toMap

    inline private def positionFrom(positionData: PositionData): Position = {
        positionData.positionType match {
            case PositionType.OnlyCoordinates => Position(Coordinates(0, 0))
            case PositionType.CoordinatesAndDirection => Position(Coordinates(0, 0), Direction.North)
        }
    }

    def findByName(name: String): Option[Position] = positionByName.get(name)
}
