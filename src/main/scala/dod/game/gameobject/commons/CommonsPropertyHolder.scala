package dod.game.gameobject.commons

import dod.game.gameobject.GameObject
import dod.game.temporal.Timestamps.Timestamp

import java.util.UUID

private[gameobject] trait CommonsPropertyHolder {
    self: GameObject =>

    protected val commonsProperty: CommonsProperty

    final def id: UUID = self.commonsProperty.id

    final def name: String = self.commonsProperty.name

    final def creationTimestamp: Timestamp = self.commonsProperty.creationTimestamp
}
