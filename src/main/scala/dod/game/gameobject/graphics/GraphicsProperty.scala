package dod.game.gameobject.graphics

import dod.game.gameobject.position.Direction
import dod.game.gameobject.state.State

class GraphicsProperty(private[graphics] val level: Int,
                       animationSelector: AnimationSelector) {
    def animation(state: Option[State], direction: Option[Direction]): Animation =
        animationSelector.selectAnimation(state, direction)
}
