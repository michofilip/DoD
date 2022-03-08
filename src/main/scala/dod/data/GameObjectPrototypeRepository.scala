//package dod.data
//
//import dod.game.gameobject.physics.PhysicsSelector
//
//class GameObjectPrototypeRepository {
//    enum PositionType {
//        case NoPosition
//        case OnlyCoordinates
//        case CoordinatesAndDirection
//    }
//
//    case class GameObjectPrototype(name: String, position: PositionType, displayLayer: Option[Int])
//
//    private val gameObjectPrototypeByName = Seq(
//        GameObjectPrototype(name = "floor", position = PositionType.OnlyCoordinates, displayLayer = Some(0)),
//        GameObjectPrototype(name = "wall", position = PositionType.OnlyCoordinates, displayLayer = Some(2)),
//        GameObjectPrototype(name = "player", position = PositionType.CoordinatesAndDirection, displayLayer = Some(1)),
//        GameObjectPrototype(name = "door", position = PositionType.OnlyCoordinates, displayLayer = Some(2)),
//        GameObjectPrototype(name = "switch", position = PositionType.OnlyCoordinates, displayLayer = Some(2))
//    ).map(gop => gop.name -> gop).toMap
//
//    def findByName(name: String): Option[GameObjectPrototype] = gameObjectPrototypeByName.get(name)
//}
