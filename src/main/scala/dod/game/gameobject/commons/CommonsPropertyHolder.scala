package dod.game.gameobject.commons

import dod.game.gameobject.GameObject
import dod.game.model.Timestamp

import java.util.UUID

private[gameobject] trait CommonsPropertyHolder {
    self: GameObject =>

    protected val commonsProperty: CommonsProperty

    final def id: String = self.commonsProperty.id

    final def name: String = self.commonsProperty.name

    final def creationTimestamp: Timestamp = self.commonsProperty.creationTimestamp
}
