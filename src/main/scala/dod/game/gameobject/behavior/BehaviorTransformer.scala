package dod.game.gameobject.behavior

import dod.game.model.Behavior

trait BehaviorTransformer extends (Map[String, Behavior] => Map[String, Behavior])

object BehaviorTransformer {
    
    def addBehavior(key: String, behavior: Behavior): BehaviorTransformer = behaviors => behaviors + (key -> behavior)

    def removeBehavior(key: String): BehaviorTransformer = behaviors => behaviors - key

    def removeAllBehavior: BehaviorTransformer = _ => Map.empty
    
}
