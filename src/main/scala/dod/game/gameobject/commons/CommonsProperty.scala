package dod.game.gameobject.commons

import dod.game.temporal.Timestamps.Timestamp

import java.util.UUID

final class CommonsProperty(private[commons] val id: UUID,
                            private[commons] val name: String,
                            private[commons] val creationTimestamp: Timestamp)

object CommonsProperty {
    def apply(id: UUID, name: String, creationTimestamp: Timestamp): CommonsProperty =
        new CommonsProperty(id, name, creationTimestamp)
}