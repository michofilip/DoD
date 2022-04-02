package dod.game.gameobject.state

import dod.game.model.State
import dod.game.model.Timestamps.Timestamp

trait StateAccessor {
    def state: Option[State]

    def stateTimestamp: Option[Timestamp]
}
