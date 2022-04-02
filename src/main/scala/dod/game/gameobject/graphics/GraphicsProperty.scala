package dod.game.gameobject.graphics

import dod.game.model.{Animation, AnimationSelector, Direction, State}

class GraphicsProperty(animationSelector: AnimationSelector) {
    def animation(state: Option[State], direction: Option[Direction]): Animation =
        animationSelector.selectAnimation(state, direction)
}
