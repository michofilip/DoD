package dod.game.gameobject.state

import dod.game.gameobject.GameObject
import dod.game.temporal.Timestamps.Timestamp

private[gameobject] trait StatePropertyHolder {
    self: GameObject =>

    protected val stateProperty: Option[StateProperty]

    final val stateAccessor = new StateAccessor {
        override def state: Option[State] = self.stateProperty.map(_.state)

        override def stateTimestamp: Option[Timestamp] = self.stateProperty.map(_.stateTimestamp)
    }

    final def updateState(stateTransformer: StateTransformer, timestamp: Timestamp): GameObject =
        update(stateProperty = self.stateProperty.map(_.updatedState(stateTransformer, timestamp)))

}
