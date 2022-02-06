package dod.gameobject

import dod.gameobject.commons.{CommonsProperty, CommonsPropertyHolder}
import dod.gameobject.graphics.{GraphicsProperty, GraphicsPropertyHolder}
import dod.gameobject.physics.{PhysicsProperty, PhysicsPropertyHolder}
import dod.gameobject.position.{PositionProperty, PositionPropertyHolder, PositionTransformer}
import dod.gameobject.state.{StateProperty, StatePropertyHolder, StateTransformer}
import dod.temporal.Timestamps.Timestamp

import java.util.UUID

final class GameObject(val id: UUID,
                       override protected val commonsProperty: CommonsProperty,
                       override protected val positionProperty: Option[PositionProperty] = None,
                       override protected val stateProperty: Option[StateProperty] = None,
                       override protected val physicsProperty: Option[PhysicsProperty] = None,
                       override protected val graphicsProperty: Option[GraphicsProperty] = None)
    extends CommonsPropertyHolder
        with PositionPropertyHolder[GameObject]
        with StatePropertyHolder[GameObject]
        with PhysicsPropertyHolder[GameObject]
        with GraphicsPropertyHolder[GameObject] {

    inline private def update(positionProperty: Option[PositionProperty] = positionProperty,
                              stateProperty: Option[StateProperty] = stateProperty): GameObject =
        new GameObject(
            id = id,
            commonsProperty = commonsProperty,
            positionProperty = positionProperty,
            stateProperty = stateProperty,
            physicsProperty = physicsProperty,
            graphicsProperty = graphicsProperty
        )

    override def updatePosition(positionTransformer: PositionTransformer, timestamp: Timestamp): GameObject =
        update(positionProperty = positionProperty.map(_.updatePosition(positionTransformer, timestamp)))

    override def updateState(stateTransformer: StateTransformer, timestamp: Timestamp): GameObject =
        update(stateProperty = stateProperty.map(_.updatedState(stateTransformer, timestamp)))

}


//object GameObject {
//
//    def apply(id: UUID, commonsProperty: CommonsProperty): GameObject = {
//        new GameObject(id, commonsProperty)
//    }
//
//    extension (gameObject: GameObject) {
//        def withPositionProperty(positionProperty: PositionProperty): GameObject = {
//            new GameObject(
//                id = gameObject.id,
//                commonsProperty = gameObject.commonsProperty,
//                positionProperty = Some(positionProperty),
//                stateProperty = gameObject.stateProperty,
//                physicsProperty = gameObject.physicsProperty
//            )
//        }
//
//        def withStateProperty(stateProperty: StateProperty): GameObject = {
//            new GameObject(
//                id = gameObject.id,
//                commonsProperty = gameObject.commonsProperty,
//                positionProperty = gameObject.positionProperty,
//                stateProperty = Some(stateProperty),
//                physicsProperty = gameObject.physicsProperty
//            )
//        }
//
//        def withPhysicsProperty(physicsProperty: PhysicsProperty): GameObject = {
//            new GameObject(
//                id = gameObject.id,
//                commonsProperty = gameObject.commonsProperty,
//                positionProperty = gameObject.positionProperty,
//                stateProperty = gameObject.stateProperty,
//                physicsProperty = Some(physicsProperty)
//            )
//        }
//    }
//}