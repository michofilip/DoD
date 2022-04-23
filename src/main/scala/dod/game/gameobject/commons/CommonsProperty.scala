package dod.game.gameobject.commons

import dod.game.model.Timestamps.Timestamp

import java.util.UUID

final class CommonsProperty(private[commons] val id: String,
                            private[commons] val name: String,
                            private[commons] val creationTimestamp: Timestamp)
