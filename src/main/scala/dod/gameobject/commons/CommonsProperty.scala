package dod.gameobject.commons

import dod.temporal.Timestamps.Timestamp

import java.util.UUID

final class CommonsProperty(private[commons] val id: UUID,
                            private[commons] val name: String,
                            private[commons] val creationTimestamp: Timestamp)