package dod.game.gameobject.state

trait StateTransformer extends (State => State)

object StateTransformer {

    def switchOff: StateTransformer = {
        case State.On => State.Off
        case state => state
    }

    def switchOn: StateTransformer = {
        case State.Off => State.On
        case state => state
    }

    def switch: StateTransformer = {
        case State.On => State.Off
        case State.Off => State.On
        case state => state
    }

    def open: StateTransformer = {
        case State.Closed => State.Open
        case state => state
    }

    def close: StateTransformer = {
        case State.Open => State.Closed
        case state => state
    }
}