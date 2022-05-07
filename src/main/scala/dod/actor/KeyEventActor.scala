package dod.actor

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import akka.util.Collections
import dod.actor.KeyEventActor.Command
import dod.game.gameobject.GameObjectRepository
import dod.service.KeyEventService
import scalafx.scene.input.KeyEvent

import scala.collection.immutable.{AbstractSeq, LinearSeq}

final class KeyEventActor private(keyEventService: KeyEventService, gameStageActor: ActorRef[GameStageActor.Command]) {
    private def behavior(): Behavior[Command] = Behaviors.receiveMessage {
        case KeyEventActor.ProcessKeyEvent(gameObjectRepository, keyEvent) => handleProcessKeyEvent(gameObjectRepository, keyEvent)
    }

    private inline def handleProcessKeyEvent(gameObjectRepository: GameObjectRepository, keyEvent: KeyEvent): Behavior[Command] = {
        val events = keyEventService.processKeyEvent(gameObjectRepository, keyEvent)
        if (events.nonEmpty) {
            gameStageActor ! GameStageActor.AddEvents(events)
        }
        Behaviors.same
    }
}

object KeyEventActor {

    sealed trait Command

    private[actor] final case class ProcessKeyEvent(gameObjectRepository: GameObjectRepository, keyEvent: KeyEvent) extends Command

    def apply(keyEventService: KeyEventService, gameStageActor: ActorRef[GameStageActor.Command]): Behavior[Command] =
        new KeyEventActor(keyEventService, gameStageActor).behavior()
}