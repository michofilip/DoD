package dod.game.event

import dod.game.model.{Coordinates, Direction, Shift}

import java.util.UUID

enum PositionEvent extends Event {
    case MoveTo(gameObjectId: UUID, coordinates: Coordinates)
    case MoveBy(gameObjectId: UUID, shift: Shift)
    case TurnTo(gameObjectId: UUID, direction: Direction)
    case TurnClockwise(gameObjectId: UUID)
    case TurnCounterClockwise(gameObjectId: UUID)
    case TurnBack(gameObjectId: UUID)
    case Step(gameObjectId: UUID, direction: Direction)
    case StepForward(gameObjectId: UUID)
    case StepRight(gameObjectId: UUID)
    case StepLeft(gameObjectId: UUID)
    case StepBack(gameObjectId: UUID)
    case StepAndFace(gameObjectId: UUID, direction: Direction)
    case StepRightAndFace(gameObjectId: UUID)
    case StepLeftAndFace(gameObjectId: UUID)
    case StepBackAndFace(gameObjectId: UUID)
}
