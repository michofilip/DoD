package dod.game.gameobject.position

import dod.game.gameobject.position.Direction.{East, North, South, West}

import scala.annotation.showAsInfix
import scala.util.chaining.scalaUtilChainingOps

trait PositionTransformer extends (Position => Position)

object PositionTransformer {

    def moveTo(coordinates: Coordinates): PositionTransformer = position => position.copy(coordinates = coordinates)

    def moveBy(shift: Shift): PositionTransformer = position => moveTo(position.coordinates.moveBy(shift))(position)

    def turnTo(direction: Direction): PositionTransformer = position => position.copy(direction = direction)

    def turnClockwise: PositionTransformer = position => position.direction match {
        case North => turnTo(East)(position)
        case East => turnTo(South)(position)
        case South => turnTo(West)(position)
        case West => turnTo(North)(position)
    }

    def turnCounterClockwise: PositionTransformer = position => position.direction match {
        case North => turnTo(West)(position)
        case East => turnTo(North)(position)
        case South => turnTo(East)(position)
        case West => turnTo(South)(position)
    }

    def turnBack: PositionTransformer = position => position.direction match {
        case North => turnTo(South)(position)
        case East => turnTo(West)(position)
        case South => turnTo(North)(position)
        case West => turnTo(East)(position)
    }

    def stepForward: PositionTransformer = position => position.direction match {
        case North => moveBy(Shift(0, 1))(position)
        case East => moveBy(Shift(1, 0))(position)
        case South => moveBy(Shift(0, -1))(position)
        case West => moveBy(Shift(-1, 0))(position)
    }

    def stepRight: PositionTransformer = position => position.direction match {
        case North => moveBy(Shift(1, 0))(position)
        case East => moveBy(Shift(0, -1))(position)
        case South => moveBy(Shift(-1, 0))(position)
        case West => moveBy(Shift(0, 1))(position)
    }

    def stepLeft: PositionTransformer = position => position.direction match {
        case North => moveBy(Shift(-1, 0))(position)
        case East => moveBy(Shift(0, 1))(position)
        case South => moveBy(Shift(1, 0))(position)
        case West => moveBy(Shift(0, -1))(position)
    }

    def stepBack: PositionTransformer = position => position.direction match {
        case North => moveBy(Shift(0, -1))(position)
        case East => moveBy(Shift(-1, 0))(position)
        case South => moveBy(Shift(0, 1))(position)
        case West => moveBy(Shift(1, 0))(position)
    }

    def stepRightAndFace: PositionTransformer = position => (turnClockwise andThen stepForward) (position)

    def stepLeftAndFace: PositionTransformer = position => (turnCounterClockwise andThen stepForward) (position)

    def stepBackAndFace: PositionTransformer = position => (turnBack andThen stepForward) (position)
}