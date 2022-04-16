package dod.service.event

import dod.game.event.ScriptEvent
import dod.game.gameobject.GameObjectRepository
import dod.game.model.Instruction.*
import dod.game.model.Script
import dod.service.event.EventService.EventResponse
import dod.service.expression.ExpressionService

import java.util.UUID

private[event] final class ScriptEventService(expressionService: ExpressionService) {

    def processScriptEvent(gameObjectRepository: GameObjectRepository, scriptEvent: ScriptEvent): EventResponse = scriptEvent match {
        case ScriptEvent.RunScript(gameObjectId, scriptName, lineNo) =>
            handleRunScript(gameObjectRepository, gameObjectId, scriptName, lineNo)
    }

    private inline def handleRunScript(gameObjectRepository: GameObjectRepository, gameObjectId: UUID, scriptName: String, lineNo: Int): EventResponse = {
        for
            script <- gameObjectRepository.findScript(gameObjectId, scriptName)
        yield {
            given GameObjectRepository = gameObjectRepository

            val responseEvents = script.nextExecutableLine(lineNo) match {
                case (_, EXIT(_)) => Seq.empty
                case (nextLineNo, EXECUTE(events)) => ScriptEvent.RunScript(gameObjectId, scriptName, nextLineNo + 1) +: events
                case (nextLineNo, TEST(condition)) => expressionService.resolve(condition) match {
                    case Some(true) => Seq(ScriptEvent.RunScript(gameObjectId, scriptName, nextLineNo + 2))
                    case Some(false) => Seq(ScriptEvent.RunScript(gameObjectId, scriptName, nextLineNo + 1))
                    case None => Seq.empty
                }
                case _ => Seq.empty
            }

            (gameObjectRepository, responseEvents)
        }
    }.getOrElse {
        (gameObjectRepository, Seq.empty)
    }

}
