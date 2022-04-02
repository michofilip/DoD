package dod.service

import dod.data.StateRepository
import dod.game.gameobject.state.StateProperty
import dod.game.model.Timestamps.Timestamp

class StateService(stateRepository: StateRepository) {
    def getStateProperty(name: String, timestamp: Timestamp): Option[StateProperty] = {
        stateRepository.findByName(name).map { state =>
            new StateProperty(state, timestamp)
        }
    }
}
