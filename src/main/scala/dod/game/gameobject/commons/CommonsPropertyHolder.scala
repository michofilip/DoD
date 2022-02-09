package dod.game.gameobject.commons

import dod.game.gameobject.GameObject
import dod.game.temporal.Timestamps.Timestamp

import java.util.UUID

private[gameobject] trait CommonsPropertyHolder {
    self: GameObject =>

    protected val commonsProperty: CommonsProperty

    final val commonsAccessor = new CommonsAccessor {
        override def id: UUID = self.commonsProperty.id

        override def name: String = self.commonsProperty.name

        override def creationTimestamp: Timestamp = self.commonsProperty.creationTimestamp
    }
}
