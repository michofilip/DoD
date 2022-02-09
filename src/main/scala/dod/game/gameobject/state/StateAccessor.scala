package dod.game.gameobject.state

import dod.game.temporal.Timestamps.Timestamp

trait StateAccessor {
    def state: Option[State]

    def stateTimestamp: Option[Timestamp]
}
