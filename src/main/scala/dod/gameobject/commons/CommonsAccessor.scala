package dod.gameobject.commons

import dod.temporal.Timestamps.Timestamp

import java.util.UUID

trait CommonsAccessor {
    def id: UUID

    def name: String

    def creationTimestamp: Timestamp
}
