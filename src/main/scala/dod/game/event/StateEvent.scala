package dod.game.event

import dod.game.gameobject.state.{State, StateTransformer}

import java.util.UUID

sealed trait StateEvent extends Event

object StateEvent {

    final case class SwitchOff(gameObjectId: UUID) extends StateEvent

    final case class SwitchOn(gameObjectId: UUID) extends StateEvent

    final case class Switch(gameObjectId: UUID) extends StateEvent

    final case class Open(gameObjectId: UUID) extends StateEvent

    final case class Close(gameObjectId: UUID) extends StateEvent
    
}
