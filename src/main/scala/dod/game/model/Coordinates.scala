package dod.game.model

import dod.game.model.Shift

final case class Coordinates(x: Int, y: Int) {
    def moveBy(shift: Shift): Coordinates = Coordinates(x + shift.dx, y + shift.dy)
}
