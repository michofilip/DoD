package dod.game.event

import dod.game.expression.{CoordinatesExpr, DirectionExpr, ShiftExpr, StringExpr}
import dod.game.model.{Coordinates, Direction, Shift}

enum PositionEvent extends Event {
    case MoveTo(gameObjectId: StringExpr, coordinates: CoordinatesExpr)
    case MoveBy(gameObjectId: StringExpr, shift: ShiftExpr)
    case TurnTo(gameObjectId: StringExpr, direction: DirectionExpr)
    case TurnClockwise(gameObjectId: StringExpr)
    case TurnCounterClockwise(gameObjectId: StringExpr)
    case TurnBack(gameObjectId: StringExpr)
    case Step(gameObjectId: StringExpr, direction: DirectionExpr)
    case StepForward(gameObjectId: StringExpr)
    case StepRight(gameObjectId: StringExpr)
    case StepLeft(gameObjectId: StringExpr)
    case StepBack(gameObjectId: StringExpr)
    case StepAndFace(gameObjectId: StringExpr, direction: DirectionExpr)
    case StepRightAndFace(gameObjectId: StringExpr)
    case StepLeftAndFace(gameObjectId: StringExpr)
    case StepBackAndFace(gameObjectId: StringExpr)
}
