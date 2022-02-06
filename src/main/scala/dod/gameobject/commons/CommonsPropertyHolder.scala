package dod.gameobject.commons

import dod.temporal.Timestamps.Timestamp

private[gameobject] trait CommonsPropertyHolder {
    protected val commonsProperty: CommonsProperty

    final val commonsData = new CommonsData {
        override def name: String = commonsProperty.name

        override def creationTimestamp: Timestamp = commonsProperty.creationTimestamp
    }
}
