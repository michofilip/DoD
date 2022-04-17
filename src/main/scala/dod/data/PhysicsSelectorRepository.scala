package dod.data

import dod.game.model.{PhysicsSelector, State}

class PhysicsSelectorRepository(physicsRepository: PhysicsRepository) {
    case class PhysicsSelectorVariant(name: String, state: Option[State], physicsId: Int)

    private val physicsSelectorByName = Seq(
        PhysicsSelectorVariant(name = "floor", state = None, physicsId = 1),
        PhysicsSelectorVariant(name = "wall", state = None, physicsId = 2),
        PhysicsSelectorVariant(name = "player", state = None, physicsId = 2),
        PhysicsSelectorVariant(name = "door", state = Some(State.Open), physicsId = 1),
        PhysicsSelectorVariant(name = "door", state = Some(State.Closed), physicsId = 2),
        PhysicsSelectorVariant(name = "switch", state = Some(State.Off), physicsId = 2),
        PhysicsSelectorVariant(name = "switch", state = Some(State.On), physicsId = 2)
    ).groupBy(_.name).transform { case (_, variants) =>
        val variantMapped = variants.map { variant =>
            variant.state -> physicsRepository.findById(variant.physicsId)
        }.toMap

        new PhysicsSelector(variantMapped)
    }

    def findByName(name: String): Option[PhysicsSelector] = physicsSelectorByName.get(name)
}
