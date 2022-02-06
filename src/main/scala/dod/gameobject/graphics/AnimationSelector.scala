package dod.gameobject.graphics

import dod.gameobject.position.Direction
import dod.gameobject.state.State

final class AnimationSelector(variants: Map[(Option[State], Option[Direction]), Animation]) {
    inline def selectAnimation(state: Option[State], direction: Option[Direction]): Animation = variants((state, direction))
}

object AnimationSelector {
    def apply(variants: ((Option[State], Option[Direction]), Animation)*): AnimationSelector =
        new AnimationSelector(variants.toMap)
}
