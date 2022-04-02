package dod.game.gameobject.position

import dod.game.model.{Coordinates, Direction, Position, Shift}
import dod.game.model.Direction.{East, North, South, West}

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
        case Some(direction) => turnTo(direction.nextClockwise)(position)
        case None => position
    }

    def turnCounterClockwise: PositionTransformer = position => position.direction match {
        case Some(direction) => turnTo(direction.nextCounterClockwise)(position)
        case None => position
    }

    def turnBack: PositionTransformer = position => position.direction match {
        case Some(direction) => turnTo(direction.opposite)(position)
        case None => position
    }

    def step(direction: Direction): PositionTransformer = position => moveBy(direction.shift)(position)

    def stepForward: PositionTransformer = position => position.direction match {
        case Some(direction) => step(direction)(position)
        case None => position
    }

    def stepRight: PositionTransformer = position => position.direction match {
        case Some(direction) => step(direction.nextClockwise)(position)
        case None => position
    }

    def stepLeft: PositionTransformer = position => position.direction match {
        case Some(direction) => step(direction.nextCounterClockwise)(position)
        case None => position
    }

    def stepBack: PositionTransformer = position => position.direction match {
        case Some(direction) => step(direction.opposite)(position)
        case None => position
    }

    def stepAndFace(direction: Direction): PositionTransformer = position => (turnTo(direction) andThen stepForward) (position)

    def stepRightAndFace: PositionTransformer = position => (turnClockwise andThen stepForward) (position)

    def stepLeftAndFace: PositionTransformer = position => (turnCounterClockwise andThen stepForward) (position)

    def stepBackAndFace: PositionTransformer = position => (turnBack andThen stepForward) (position)
}
