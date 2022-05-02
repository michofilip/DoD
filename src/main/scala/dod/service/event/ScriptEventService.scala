package dod.service.event

import dod.game.event.ScriptEvent
import dod.game.expression.Expr
import dod.game.gameobject.GameObjectRepository
import dod.game.model.Instruction.*
import dod.game.model.Script
import dod.service.ScriptService
import dod.service.event.EventService.*

import java.util.UUID

private[event] final class ScriptEventService {

    private[event] def processScriptEvent(scriptEvent: ScriptEvent)(using gameObjectRepository: GameObjectRepository): EventResponse = scriptEvent match {
        case ScriptEvent.RunScript(gameObjectId, scriptName, lineNo) => (gameObjectId, scriptName) ~> {
            (gameObjectId, scriptName) => handleRunScript(gameObjectId, scriptName, lineNo)
        }
    }

    private inline def handleRunScript(gameObjectId: String, scriptName: String, lineNo: Int)(using gameObjectRepository: GameObjectRepository): EventResponse = {
        for
            script <- gameObjectRepository.findScript(gameObjectId, scriptName)
        yield {
            val responseEvents = ScriptService.nextExecutableLine(script, lineNo) match {
                case (_, EXIT(_)) => Seq.empty
                // TODO possibly slow
                case (nextLineNo, EXECUTE(events)) => events :+ ScriptEvent.RunScript(Expr(gameObjectId), Expr(scriptName), nextLineNo + 1)
                case _ => Seq.empty
            }

            (gameObjectRepository, responseEvents)
        }
    }.getOrElse {
        defaultResponse
    }

}
