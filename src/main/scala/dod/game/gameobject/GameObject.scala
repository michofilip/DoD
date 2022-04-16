package dod.game.gameobject

import dod.game.gameobject.behavior.{BehaviorProperty, BehaviorPropertyHolder}
import dod.game.gameobject.commons.{CommonsProperty, CommonsPropertyHolder}
import dod.game.gameobject.graphics.{GraphicsProperty, GraphicsPropertyHolder}
import dod.game.gameobject.physics.{PhysicsProperty, PhysicsPropertyHolder}
import dod.game.gameobject.position.{PositionProperty, PositionPropertyHolder, PositionTransformer}
import dod.game.gameobject.scheduler.{SchedulerProperty, SchedulerPropertyHolder}
import dod.game.gameobject.script.{ScriptProperty, ScriptPropertyHolder}
import dod.game.gameobject.state.{StateProperty, StatePropertyHolder, StateTransformer}
import dod.game.gameobject.timer.{TimersProperty, TimersPropertyHolder}
import dod.game.model.Timestamps.Timestamp

final class GameObject(override protected val commonsProperty: CommonsProperty,
                       override protected val positionProperty: Option[PositionProperty] = None,
                       override protected val stateProperty: Option[StateProperty] = None,
                       override protected val physicsProperty: Option[PhysicsProperty] = None,
                       override protected val graphicsProperty: Option[GraphicsProperty] = None,
                       override protected val timersProperty: Option[TimersProperty] = None,
                       override protected val schedulerProperty: Option[SchedulerProperty] = None,
                       override protected val behaviorProperty: Option[BehaviorProperty] = None,
                       override protected val scriptProperty: Option[ScriptProperty] = None)
    extends CommonsPropertyHolder
        with PositionPropertyHolder
        with StatePropertyHolder
        with PhysicsPropertyHolder
        with GraphicsPropertyHolder
        with TimersPropertyHolder
        with SchedulerPropertyHolder
        with BehaviorPropertyHolder
        with ScriptPropertyHolder {

    inline protected def update(positionProperty: Option[PositionProperty] = positionProperty,
                                stateProperty: Option[StateProperty] = stateProperty,
                                timersProperty: Option[TimersProperty] = timersProperty,
                                schedulerProperty: Option[SchedulerProperty] = schedulerProperty,
                                behaviorProperty: Option[BehaviorProperty] = behaviorProperty,
                                scriptProperty: Option[ScriptProperty] = scriptProperty): GameObject =
        GameObject(
            commonsProperty = commonsProperty,
            positionProperty = positionProperty,
            stateProperty = stateProperty,
            physicsProperty = physicsProperty,
            graphicsProperty = graphicsProperty,
            timersProperty = timersProperty,
            schedulerProperty = schedulerProperty,
            behaviorProperty = behaviorProperty,
            scriptProperty = scriptProperty
        )

}
