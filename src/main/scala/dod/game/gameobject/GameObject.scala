package dod.game.gameobject

import dod.game.gameobject.commons.{CommonsProperty, CommonsPropertyHolder}
import dod.game.gameobject.graphics.{GraphicsProperty, GraphicsPropertyHolder}
import dod.game.gameobject.physics.{PhysicsProperty, PhysicsPropertyHolder}
import dod.game.gameobject.position.{PositionProperty, PositionPropertyHolder, PositionTransformer}
import dod.game.gameobject.scheduler.{SchedulerProperty, SchedulerPropertyHolder}
import dod.game.gameobject.state.{StateProperty, StatePropertyHolder, StateTransformer}
import dod.game.gameobject.timer.{TimersProperty, TimersPropertyHolder}
import dod.game.temporal.Timestamps.Timestamp

final class GameObject(override protected val commonsProperty: CommonsProperty,
                       override protected val positionProperty: Option[PositionProperty] = None,
                       override protected val stateProperty: Option[StateProperty] = None,
                       override protected val physicsProperty: Option[PhysicsProperty] = None,
                       override protected val graphicsProperty: Option[GraphicsProperty] = None,
                       override protected val timersProperty: Option[TimersProperty] = None,
                       override protected val schedulerProperty: Option[SchedulerProperty] = None)
    extends CommonsPropertyHolder
        with PositionPropertyHolder
        with StatePropertyHolder
        with PhysicsPropertyHolder
        with GraphicsPropertyHolder
        with TimersPropertyHolder
        with SchedulerPropertyHolder {

    inline protected def update(positionProperty: Option[PositionProperty] = positionProperty,
                                stateProperty: Option[StateProperty] = stateProperty,
                                timersProperty: Option[TimersProperty] = timersProperty,
                                schedulerProperty: Option[SchedulerProperty] = schedulerProperty): GameObject =
        GameObject(
            commonsProperty = commonsProperty,
            positionProperty = positionProperty,
            stateProperty = stateProperty,
            physicsProperty = physicsProperty,
            graphicsProperty = graphicsProperty,
            timersProperty = timersProperty,
            schedulerProperty = schedulerProperty
        )

}
