package dod.gameobject.state

import dod.temporal.Timestamps.Timestamp

trait StateData {
    def state: Option[State]

    def stateTimestamp: Option[Timestamp]
}
