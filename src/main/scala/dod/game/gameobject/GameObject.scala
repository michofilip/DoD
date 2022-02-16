package dod.game.gameobject

import dod.game.gameobject.commons.{CommonsProperty, CommonsPropertyHolder}
import dod.game.gameobject.graphics.{GraphicsProperty, GraphicsPropertyHolder}
import dod.game.gameobject.physics.{PhysicsProperty, PhysicsPropertyHolder}
import dod.game.gameobject.position.{PositionProperty, PositionPropertyHolder, PositionTransformer}
import dod.game.gameobject.state.{StateProperty, StatePropertyHolder, StateTransformer}
import dod.game.temporal.Timestamps.Timestamp

final class GameObject(override protected val commonsProperty: CommonsProperty,
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
        GameObject(
            commonsProperty = commonsProperty,
            positionProperty = positionProperty,
            stateProperty = stateProperty,
            physicsProperty = physicsProperty,
            graphicsProperty = graphicsProperty
        )

    override def toString: String = {
        Seq(
            Option(commonsAccessor.id).map(id => s"id=$id"),
            Option(commonsAccessor.name).map(name => s"name=$name"),
            Option(commonsAccessor.creationTimestamp).map(creationTimestamp => s"creationTimestamp=$creationTimestamp"),
            positionAccessor.coordinates.map(coordinates => s"coordinates=$coordinates"),
            positionAccessor.direction.map(direction => s"direction=$direction"),
            positionAccessor.positionTimestamp.map(positionTimestamp => s"positionTimestamp=$positionTimestamp"),
            stateAccessor.state.map(state => s"state=$state"),
            stateAccessor.stateTimestamp.map(stateTimestamp => s"stateTimestamp=$stateTimestamp"),
        ).flatten.mkString("GameObject(", ",", ")")
    }
}
