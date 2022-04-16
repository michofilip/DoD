package dod.game.gameobject.behavior

import dod.game.model.Behavior

trait BehaviorTransformer extends (Map[String, Behavior] => Map[String, Behavior])

object BehaviorTransformer {
    
    def addBehavior(behaviorName: String, behavior: Behavior): BehaviorTransformer = behaviors => behaviors + (behaviorName -> behavior)

    def removeBehavior(behaviorName: String): BehaviorTransformer = behaviors => behaviors - behaviorName

    def removeAllBehavior: BehaviorTransformer = _ => Map.empty
    
}
