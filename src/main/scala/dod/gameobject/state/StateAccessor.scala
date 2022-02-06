package dod.gameobject.state

import dod.temporal.Timestamps.Timestamp

trait StateAccessor {
    def state: Option[State]

    def stateTimestamp: Option[Timestamp]
}
