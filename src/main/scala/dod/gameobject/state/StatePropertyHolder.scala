package dod.gameobject.state

import dod.gameobject.GameObject
import dod.temporal.Timestamps.Timestamp

private[gameobject] trait StatePropertyHolder[T <: GameObject] {
    protected val stateProperty: Option[StateProperty]

    final val stateData = new StateData {
        override def state: Option[State] = stateProperty.map(_.state)

        override def stateTimestamp: Option[Timestamp] = stateProperty.map(_.stateTimestamp)
    }

    def updateState(stateTransformer: StateTransformer, timestamp: Timestamp): T

}
