package dod.game.model

import dod.game.model.Instruction.*

import scala.annotation.tailrec

final case class Script(instructions: IndexedSeq[Instruction]) {

    private val scriptLength = instructions.length

    private val labelMap: Map[Int, Int] =
        instructions.zipWithIndex.collect {
            case (LABEL(labelId), lineNo) => labelId -> lineNo
        }.toMap


    @tailrec
    def nextExecutableLine(lineNo: Int): (Int, Instruction) =
        getInstruction(lineNo) match {
            case LABEL(_) => nextExecutableLine(lineNo + 1)
            case GOTO(labelId) => labelMap.get(labelId) match {
                case Some(lineNo) => nextExecutableLine(lineNo + 1)
                case None => (lineNo, EXIT(2))
            }
            case instruction => (lineNo, instruction)
        }

    private inline def getInstruction(lineNo: Int): Instruction =
        if (0 <= lineNo && lineNo < scriptLength)
            instructions(lineNo)
        else
            EXIT(1)

}
