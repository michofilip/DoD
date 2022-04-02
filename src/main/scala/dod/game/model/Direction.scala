package dod.game.model

import dod.game.model.Shift

enum Direction {
    case North
    case East
    case South
    case West

    def nextClockwise: Direction = this match {
        case North => East
        case East => South
        case South => West
        case West => North
    }

    def nextCounterClockwise: Direction = this match {
        case North => West
        case East => North
        case South => East
        case West => South
    }

    def opposite: Direction = this match {
        case North => South
        case East => West
        case South => North
        case West => East
    }

    def shift: Shift = this match {
        case North => Shift(0, -1)
        case East => Shift(1, 0)
        case South => Shift(0, 1)
        case West => Shift(-1, 0)
    }

}
