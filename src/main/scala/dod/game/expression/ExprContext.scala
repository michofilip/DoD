package dod.game.expression

import dod.game.GameStage

trait ExprContext:
    self: GameStage =>

    def gameStage: GameStage = self


