package dod.game.gameobject.state

import dod.game.model.{State, Timestamp}

trait StateAccessor {
    def state: Option[State]

    def stateTimestamp: Option[Timestamp]
}
