package dod.game.gameobject.position

import Direction.{East, North, South, West}

import scala.annotation.showAsInfix
import scala.util.chaining.scalaUtilChainingOps

trait PositionTransformer extends ((Coordinates, Direction) => (Coordinates, Direction)) {
    infix def andThen(that: PositionTransformer): PositionTransformer =
        (coordinates, direction) => apply(coordinates, direction) match {
            case (coordinates, direction) => that(coordinates, direction)
        }
}

object PositionTransformer {

    def moveTo(coordinates: Coordinates): PositionTransformer = (_, direction) => (coordinates, direction)

    def moveBy(shift: Shift): PositionTransformer = (coordinates, direction) => moveTo(coordinates.moveBy(shift))(coordinates, direction)

    def turnTo(direction: Direction): PositionTransformer = (coordinates, _) => (coordinates, direction)

    def turnClockwise: PositionTransformer = (coordinates, direction) => direction match {
        case North => turnTo(East)(coordinates, direction)
        case East => turnTo(South)(coordinates, direction)
        case South => turnTo(West)(coordinates, direction)
        case West => turnTo(North)(coordinates, direction)
    }

    def turnCounterClockwise: PositionTransformer = (coordinates, direction) => direction match {
        case North => turnTo(West)(coordinates, direction)
        case East => turnTo(North)(coordinates, direction)
        case South => turnTo(East)(coordinates, direction)
        case West => turnTo(South)(coordinates, direction)
    }

    def turnBack: PositionTransformer = (coordinates, direction) => direction match {
        case North => turnTo(South)(coordinates, direction)
        case East => turnTo(West)(coordinates, direction)
        case South => turnTo(North)(coordinates, direction)
        case West => turnTo(East)(coordinates, direction)
    }

    def stepForward: PositionTransformer = (coordinates, direction) => direction match {
        case North => moveBy(Shift(0, 1))(coordinates, direction)
        case East => moveBy(Shift(1, 0))(coordinates, direction)
        case South => moveBy(Shift(0, -1))(coordinates, direction)
        case West => moveBy(Shift(-1, 0))(coordinates, direction)
    }

    def stepRight: PositionTransformer = (coordinates, direction) => direction match {
        case North => moveBy(Shift(1, 0))(coordinates, direction)
        case East => moveBy(Shift(0, -1))(coordinates, direction)
        case South => moveBy(Shift(-1, 0))(coordinates, direction)
        case West => moveBy(Shift(0, 1))(coordinates, direction)
    }

    def stepLeft: PositionTransformer = (coordinates, direction) => direction match {
        case North => moveBy(Shift(-1, 0))(coordinates, direction)
        case East => moveBy(Shift(0, 1))(coordinates, direction)
        case South => moveBy(Shift(1, 0))(coordinates, direction)
        case West => moveBy(Shift(0, -1))(coordinates, direction)
    }

    def stepBack: PositionTransformer = (coordinates, direction) => direction match {
        case North => moveBy(Shift(0, -1))(coordinates, direction)
        case East => moveBy(Shift(-1, 0))(coordinates, direction)
        case South => moveBy(Shift(0, 1))(coordinates, direction)
        case West => moveBy(Shift(1, 0))(coordinates, direction)
    }

    def stepRightAndFace: PositionTransformer = (coordinates, direction) => (turnClockwise andThen stepForward) (coordinates, direction)

    def stepLeftAndFace: PositionTransformer = (coordinates, direction) => (turnCounterClockwise andThen stepForward) (coordinates, direction)

    def stepBackAndFace: PositionTransformer = (coordinates, direction) => (turnBack andThen stepForward) (coordinates, direction)
}