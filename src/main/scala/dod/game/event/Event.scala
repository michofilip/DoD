package dod.game.event

import dod.game.gameobject.position.{Coordinates, Shift}

import java.util.UUID

enum Event {
    case MoveTo(gameObjectId: UUID, coordinates: Coordinates) extends Event
//    case MoveBy(gameObjectId: UUID, shift: Shift) extends Event
//    case StartTimer extends Event
//    case StopTimer extends Event
//    case Remove(gameObjectIds: Seq[UUID]) extends Event
//    case Create(useCurrentTimestamp: Boolean, gameObjects: Seq[GameObjectEntity], events: Seq[Event]) extends Event
}
