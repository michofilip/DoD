package dod.actor

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import akka.util.Collections
import dod.actor.KeyEventActor.{Command, ProcessKeyEvent}
import dod.game.GameStage
import dod.game.gameobject.GameObjectRepository
import dod.service.KeyEventService
import scalafx.scene.input.KeyEvent

import scala.collection.immutable.{AbstractSeq, LinearSeq}

final class KeyEventActor private(keyEventService: KeyEventService, gameStageActor: ActorRef[GameStageActor.Command]) {
    private def behavior(): Behavior[Command] = Behaviors.receiveMessage {
        case ProcessKeyEvent(gameStage, keyEvent) => handleProcessKeyEvent(gameStage, keyEvent)
    }

    private inline def handleProcessKeyEvent(gameStage: GameStage, keyEvent: KeyEvent): Behavior[Command] = {
        val events = keyEventService.processKeyEvent(gameStage, keyEvent)
        if (events.nonEmpty) {
            gameStageActor ! GameStageActor.AddEvents(events)
        }
        Behaviors.same
    }
}

object KeyEventActor {

    sealed trait Command

    private[actor] final case class ProcessKeyEvent(gameStage: GameStage, keyEvent: KeyEvent) extends Command

    def apply(keyEventService: KeyEventService, gameStageActor: ActorRef[GameStageActor.Command]): Behavior[Command] =
        new KeyEventActor(keyEventService, gameStageActor).behavior()
}