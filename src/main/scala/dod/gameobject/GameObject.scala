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
        with PositionPropertyHolder
        with StatePropertyHolder
        with PhysicsPropertyHolder
        with GraphicsPropertyHolder {

    inline protected def update(positionProperty: Option[PositionProperty] = positionProperty,
                                stateProperty: Option[StateProperty] = stateProperty): GameObject =
        new GameObject(
            id = id,
            commonsProperty = commonsProperty,
            positionProperty = positionProperty,
            stateProperty = stateProperty,
            physicsProperty = physicsProperty,
            graphicsProperty = graphicsProperty
        )
}
