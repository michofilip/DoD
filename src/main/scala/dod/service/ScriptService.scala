package dod.service

import dod.game.gameobject.GameObjectRepository
import dod.game.model.Instruction.*
import dod.game.model.{Instruction, Script}

import scala.annotation.tailrec

object ScriptService {

    @tailrec
    def nextExecutableLine(script: Script, lineNo: Int)(using GameObjectRepository): (Int, Instruction) = {
        inline def getInstruction(lineNo: Int): Instruction =
            if 0 <= lineNo && lineNo < script.instructions.length then
                script.instructions(lineNo)
            else
                EXIT(1)

        getInstruction(lineNo) match
            case LABEL(_) => nextExecutableLine(script, lineNo + 1)

            case GOTO(labelId) => script.labels.get(labelId) match
                case Some(lineNo) => nextExecutableLine(script, lineNo + 1)
                case None => (lineNo, EXIT(2))

            case TEST(condition) => condition.get match
                case Some(true) => nextExecutableLine(script, lineNo + 2)
                case Some(false) => nextExecutableLine(script, lineNo + 1)
                case None => (lineNo, EXIT(3))

            case instruction => (lineNo, instruction)
    }

}
