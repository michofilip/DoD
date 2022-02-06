package dod.gameobject.commons

import dod.temporal.Timestamps.Timestamp

trait CommonsAccessor {
    def name: String

    def creationTimestamp: Timestamp
}
