package dod.gameobject.commons

import dod.gameobject.GameObject
import dod.temporal.Timestamps.Timestamp

private[gameobject] trait CommonsPropertyHolder {
    self: GameObject =>
    
    protected val commonsProperty: CommonsProperty

    final val commonsAccessor = new CommonsAccessor {
        override def name: String = self.commonsProperty.name

        override def creationTimestamp: Timestamp = self.commonsProperty.creationTimestamp
    }
}
