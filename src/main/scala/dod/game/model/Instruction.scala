package dod.game.model

import dod.game.event.Event
import dod.game.expression.BooleanExpr

import scala.collection.immutable.Queue

enum Instruction {
    case EXIT(code: Int)
    case EXECUTE(events: Queue[Event])
    case LABEL(labelId: Int)
    case GOTO(labelId: Int)
    case TEST(condition: BooleanExpr)
}


