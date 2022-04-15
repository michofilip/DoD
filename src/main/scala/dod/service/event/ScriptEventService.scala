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
        case ScriptEvent.RunScript(gameObjectId, scriptName) => ???

        case ScriptEvent.ExecuteScriptLine(script, lineNo) =>
            handleExecuteScriptLine(gameObjectRepository, script, lineNo)
    }

    private def handleExecuteScriptLine(gameObjectRepository: GameObjectRepository, script: Script, lineNo: Int): EventResponse = {
        given GameObjectRepository = gameObjectRepository

        val responseEvents = script.getNext(lineNo) match {
            case (_, EXIT(_)) =>
                Seq.empty

            case (nextLineNo, EXECUTE(events)) =>
                ScriptEvent.ExecuteScriptLine(script, nextLineNo + 1) +: events

            case (nextLineNo, TEST(condition)) =>
                expressionService.resolve(condition) match {
                    case Some(true) => Seq(ScriptEvent.ExecuteScriptLine(script, nextLineNo + 2))
                    case Some(false) => Seq(ScriptEvent.ExecuteScriptLine(script, nextLineNo + 1))
                    case None => Seq.empty
                }

            case _ =>
                Seq.empty
        }

        (gameObjectRepository, responseEvents)
    }

}
