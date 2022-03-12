package dod.game.event

import dod.game.gameobject.position.{Coordinates, Direction, Shift}

import java.util.UUID

sealed trait PositionEvent extends Event

object PositionEvent {
    final case class MoveTo(gameObjectId: UUID, coordinates: Coordinates) extends PositionEvent

    final case class MoveBy(gameObjectId: UUID, shift: Shift) extends PositionEvent

    final case class TurnTo(gameObjectId: UUID, direction: Direction) extends PositionEvent

    final case class TurnClockwise(gameObjectId: UUID) extends PositionEvent

    final case class TurnCounterClockwise(gameObjectId: UUID) extends PositionEvent

    final case class TurnBack(gameObjectId: UUID) extends PositionEvent

    final case class Step(gameObjectId: UUID, direction: Direction) extends PositionEvent

    final case class StepForward(gameObjectId: UUID) extends PositionEvent

    final case class StepRight(gameObjectId: UUID) extends PositionEvent

    final case class StepLeft(gameObjectId: UUID) extends PositionEvent

    final case class StepBack(gameObjectId: UUID) extends PositionEvent

    final case class StepAndFace(gameObjectId: UUID, direction: Direction) extends PositionEvent

    final case class StepRightAndFace(gameObjectId: UUID) extends PositionEvent

    final case class StepLeftAndFace(gameObjectId: UUID) extends PositionEvent

    final case class StepBackAndFace(gameObjectId: UUID) extends PositionEvent
}
