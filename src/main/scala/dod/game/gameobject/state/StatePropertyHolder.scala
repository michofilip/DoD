package dod.game.gameobject.state

import dod.game.gameobject.GameObject
import dod.game.model.{State, Timestamp}

private[gameobject] trait StatePropertyHolder {
    self: GameObject =>

    protected val stateProperty: Option[StateProperty]

    final val states = new StateAccessor {
        override def state: Option[State] = self.stateProperty.map(_.state)

        override def stateTimestamp: Option[Timestamp] = self.stateProperty.map(_.stateTimestamp)
    }

    final def updateState(stateTransformer: StateTransformer, timestamp: Timestamp): GameObject =
        update(stateProperty = self.stateProperty.map(_.updatedState(stateTransformer, timestamp)))

    final def withStateProperty(stateProperty: StateProperty): GameObject =
        if (self.stateProperty.isEmpty)
            update(stateProperty = Some(stateProperty))
        else this

}
