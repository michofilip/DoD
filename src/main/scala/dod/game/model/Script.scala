package dod.game.model

class Script(instructions: IndexedSeq[Instruction]) {

    import Instruction.*

    private val scriptLength = instructions.length

    private val labelMap: Map[Int, Int] =
        instructions.zipWithIndex.collect {
            case (LABEL(labelId), lineNo) => labelId -> lineNo
        }.toMap

    private def getInstruction(lineNo: Int): Instruction =
        if (0 <= lineNo && lineNo < scriptLength)
            instructions(lineNo)
        else
            EXIT(1)

    def getNext(lineNo: Int): (Int, Instruction) =
        getInstruction(lineNo) match {
            case LABEL(_) => getNext(lineNo + 1)
            case GOTO(labelId) => labelMap.get(labelId) match {
                case Some(lineNo) => getNext(lineNo + 1)
                case None => (lineNo, EXIT(2))
            }
            case instruction => (lineNo, instruction)
        }
}
