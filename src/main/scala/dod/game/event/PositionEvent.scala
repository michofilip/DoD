package dod.game.event

import dod.game.model.{Coordinates, Direction, Shift}

enum PositionEvent extends Event {
    case MoveTo(gameObjectId: String, coordinates: Coordinates)
    case MoveBy(gameObjectId: String, shift: Shift)
    case TurnTo(gameObjectId: String, direction: Direction)
    case TurnClockwise(gameObjectId: String)
    case TurnCounterClockwise(gameObjectId: String)
    case TurnBack(gameObjectId: String)
    case Step(gameObjectId: String, direction: Direction)
    case StepForward(gameObjectId: String)
    case StepRight(gameObjectId: String)
    case StepLeft(gameObjectId: String)
    case StepBack(gameObjectId: String)
    case StepAndFace(gameObjectId: String, direction: Direction)
    case StepRightAndFace(gameObjectId: String)
    case StepLeftAndFace(gameObjectId: String)
    case StepBackAndFace(gameObjectId: String)
}
