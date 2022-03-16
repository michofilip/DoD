package dod.game.event

import dod.game.gameobject.state.{State, StateTransformer}

import java.util.UUID

enum StateEvent extends Event {
    case SwitchOff(gameObjectId: UUID)
    case SwitchOn(gameObjectId: UUID)
    case Switch(gameObjectId: UUID)
    case Open(gameObjectId: UUID)
    case Close(gameObjectId: UUID)
}
