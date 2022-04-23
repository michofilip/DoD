package dod.game.event

enum StateEvent extends Event {
    case SwitchOff(gameObjectId: String)
    case SwitchOn(gameObjectId: String)
    case Switch(gameObjectId: String)
    case Open(gameObjectId: String)
    case Close(gameObjectId: String)
}
