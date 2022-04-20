package dod.game.model

import dod.game.event.Event
import dod.game.expression2.Expr2

enum Instruction {
    case EXIT(code: Int)
    case EXECUTE(events: Seq[Event])
    case LABEL(labelId: Int)
    case GOTO(labelId: Int)
    case TEST(condition: Expr2[Boolean])
}


