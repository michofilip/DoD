package dod.gameobject.state

import dod.temporal.Timestamps.Timestamp

final class StateProperty(private[state] val state: State, private[state] val stateTimestamp: Timestamp) {

    def updatedState(stateTransformer: StateTransformer, timestamp: Timestamp): StateProperty =
        stateTransformer(state) match
            case state if state != this.state => new StateProperty(state, timestamp)
            case _ => this

}
