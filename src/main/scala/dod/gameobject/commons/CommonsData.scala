package dod.gameobject.commons

import dod.temporal.Timestamps.Timestamp

trait CommonsData {
    def name: String

    def creationTimestamp: Timestamp
}
