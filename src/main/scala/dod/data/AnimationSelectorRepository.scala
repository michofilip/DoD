package dod.data

import dod.game.model.{AnimationSelector, Direction, State}

class AnimationSelectorRepository(animationRepository: AnimationRepository) {
    case class AnimationSelectorVariant(name: String, state: Option[State], direction: Option[Direction], animationId: Int)

    private val animationSelectorByName = Seq(
        AnimationSelectorVariant(name = "floor", state = None, direction = None, animationId = 1),
        AnimationSelectorVariant(name = "wall", state = None, direction = None, animationId = 2),
        AnimationSelectorVariant(name = "player", state = None, direction = Some(Direction.North), animationId = 3),
        AnimationSelectorVariant(name = "player", state = None, direction = Some(Direction.East), animationId = 4),
        AnimationSelectorVariant(name = "player", state = None, direction = Some(Direction.South), animationId = 5),
        AnimationSelectorVariant(name = "player", state = None, direction = Some(Direction.West), animationId = 6),
        AnimationSelectorVariant(name = "door", state = Some(State.Open), direction = None, animationId = 7),
        AnimationSelectorVariant(name = "door", state = Some(State.Closed), direction = None, animationId = 8),
        AnimationSelectorVariant(name = "switch", state = Some(State.Off), direction = None, animationId = 9),
        AnimationSelectorVariant(name = "switch", state = Some(State.On), direction = None, animationId = 10)
    ).groupBy(_.name).view.mapValues { variants =>
        val variantMapped = variants.map { variant =>
            (variant.state, variant.direction) -> animationRepository.findById(variant.animationId)
        }.toMap

        new AnimationSelector(variantMapped)
    }.toMap

    def findByName(name: String): Option[AnimationSelector] = animationSelectorByName.get(name)
}
