package dod.game.model

import dod.game.model.Instruction.*

import scala.annotation.tailrec

final case class Script(instructions: IndexedSeq[Instruction]) {

    val labels: Map[Int, Int] =
        instructions.zipWithIndex.collect {
            case (LABEL(labelId), lineNo) => labelId -> lineNo
        }.toMap

}
