package dod.data

import dod.game.model.State

class StateRepository {
    case class StateData(name: String, state: State)

    private val stateByName = Seq(
        StateData(name = "door", state = State.Open),
        StateData(name = "switch", state = State.Off)
    ).map { stateData =>
        stateData.name -> stateData.state
    }.toMap

    def findByName(name: String): Option[State] = stateByName.get(name)
}
