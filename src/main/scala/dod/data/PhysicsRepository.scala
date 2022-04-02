package dod.data

import dod.game.model.{Frame, Physics}

class PhysicsRepository {
    case class PhysicsData(id: Int, solid: Boolean)

    private val physicsById = Seq(
        PhysicsData(id = 1, solid = false),
        PhysicsData(id = 2, solid = true)
    ).map { physicsData =>
        physicsData.id -> physicsFrom(physicsData)
    }.toMap

    inline private def physicsFrom(physicsData: PhysicsData): Physics = {
        Physics(physicsData.solid)
    }

    def findById(id: Int): Physics = physicsById(id)
}
