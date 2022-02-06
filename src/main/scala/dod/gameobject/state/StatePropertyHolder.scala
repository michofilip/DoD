package dod.gameobject.state

import dod.gameobject.GameObject
import dod.temporal.Timestamps.Timestamp

private[gameobject] trait StatePropertyHolder {
    self: GameObject =>

    protected val stateProperty: Option[StateProperty]

    final val stateData = new StateData {
        override def state: Option[State] = self.stateProperty.map(_.state)

        override def stateTimestamp: Option[Timestamp] = self.stateProperty.map(_.stateTimestamp)
    }

    final def updateState(stateTransformer: StateTransformer, timestamp: Timestamp): GameObject =
        update(stateProperty = self.stateProperty.map(_.updatedState(stateTransformer, timestamp)))
        
}
