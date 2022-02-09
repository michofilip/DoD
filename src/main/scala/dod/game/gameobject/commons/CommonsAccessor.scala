package dod.game.gameobject.commons

import dod.game.temporal.Timestamps.Timestamp

import java.util.UUID

trait CommonsAccessor {
    def id: UUID

    def name: String

    def creationTimestamp: Timestamp
}
