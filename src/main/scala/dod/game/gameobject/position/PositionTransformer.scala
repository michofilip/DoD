package dod.game.gameobject.position

import dod.game.gameobject.position.Direction.{East, North, South, West}

import scala.annotation.showAsInfix
import scala.util.chaining.scalaUtilChainingOps

trait PositionTransformer extends (Position => Position)

object PositionTransformer {

    def moveTo(coordinates: Coordinates): PositionTransformer = position => position.copy(coordinates = coordinates)

    def moveBy(shift: Shift): PositionTransformer = position => moveTo(position.coordinates.moveBy(shift))(position)

    def turnTo(direction: Direction): PositionTransformer = position => position.direction match {
        case Some(_) => position.copy(direction = Some(direction))
        case None => position
    }

    def turnClockwise: PositionTransformer = position => position.direction match {
        case Some(North) => turnTo(East)(position)
        case Some(East) => turnTo(South)(position)
        case Some(South) => turnTo(West)(position)
        case Some(West) => turnTo(North)(position)
        case None => position
    }

    def turnCounterClockwise: PositionTransformer = position => position.direction match {
        case Some(North) => turnTo(West)(position)
        case Some(East) => turnTo(North)(position)
        case Some(South) => turnTo(East)(position)
        case Some(West) => turnTo(South)(position)
        case None => position
    }

    def turnBack: PositionTransformer = position => position.direction match {
        case Some(North) => turnTo(South)(position)
        case Some(East) => turnTo(West)(position)
        case Some(South) => turnTo(North)(position)
        case Some(West) => turnTo(East)(position)
        case None => position
    }

    def stepForward: PositionTransformer = position => position.direction match {
        case Some(North) => moveBy(Shift(0, 1))(position)
        case Some(East) => moveBy(Shift(1, 0))(position)
        case Some(South) => moveBy(Shift(0, -1))(position)
        case Some(West) => moveBy(Shift(-1, 0))(position)
        case None => position
    }

    def stepRight: PositionTransformer = position => position.direction match {
        case Some(North) => moveBy(Shift(1, 0))(position)
        case Some(East) => moveBy(Shift(0, -1))(position)
        case Some(South) => moveBy(Shift(-1, 0))(position)
        case Some(West) => moveBy(Shift(0, 1))(position)
        case None => position
    }

    def stepLeft: PositionTransformer = position => position.direction match {
        case Some(North) => moveBy(Shift(-1, 0))(position)
        case Some(East) => moveBy(Shift(0, 1))(position)
        case Some(South) => moveBy(Shift(1, 0))(position)
        case Some(West) => moveBy(Shift(0, -1))(position)
        case None => position
    }

    def stepBack: PositionTransformer = position => position.direction match {
        case Some(North) => moveBy(Shift(0, -1))(position)
        case Some(East) => moveBy(Shift(-1, 0))(position)
        case Some(South) => moveBy(Shift(0, 1))(position)
        case Some(West) => moveBy(Shift(1, 0))(position)
        case None => position
    }

    def stepRightAndFace: PositionTransformer = position => (turnClockwise andThen stepForward) (position)

    def stepLeftAndFace: PositionTransformer = position => (turnCounterClockwise andThen stepForward) (position)

    def stepBackAndFace: PositionTransformer = position => (turnBack andThen stepForward) (position)
}