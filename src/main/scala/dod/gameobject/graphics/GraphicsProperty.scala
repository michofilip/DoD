package dod.gameobject.graphics

import dod.gameobject.position.Direction
import dod.gameobject.state.State

class GraphicsProperty(val level: Int, private[graphics] val animationSelector: AnimationSelector) {
    def animation(state: Option[State], direction: Option[Direction]): Animation =
        animationSelector.selectAnimation(state, direction)
}
