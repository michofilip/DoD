package dod.game.event

import dod.game.expression.{CoordinatesExpr, DirectionExpr, ShiftExpr}
import dod.game.model.{Coordinates, Direction, Shift}

enum PositionEvent extends Event {
    case MoveTo(gameObjectId: String, coordinates: CoordinatesExpr)
    case MoveBy(gameObjectId: String, shift: ShiftExpr)
    case TurnTo(gameObjectId: String, direction: DirectionExpr)
    case TurnClockwise(gameObjectId: String)
    case TurnCounterClockwise(gameObjectId: String)
    case TurnBack(gameObjectId: String)
    case Step(gameObjectId: String, direction: DirectionExpr)
    case StepForward(gameObjectId: String)
    case StepRight(gameObjectId: String)
    case StepLeft(gameObjectId: String)
    case StepBack(gameObjectId: String)
    case StepAndFace(gameObjectId: String, direction: DirectionExpr)
    case StepRightAndFace(gameObjectId: String)
    case StepLeftAndFace(gameObjectId: String)
    case StepBackAndFace(gameObjectId: String)
}
