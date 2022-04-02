package dod.actor

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import dod.actor
import dod.actor.GameActor.Command
import dod.service.KeyEventService
import dod.service.event.EventService
import dod.ui.Screen
import scalafx.scene.input.{KeyCode, KeyEvent}

final class GameActor private(gameStageActor: ActorRef[GameStageActor.Command]) {
    private def behavior(): Behavior[Command] = Behaviors.receiveMessage {
        case GameActor.Exit =>
            Behaviors.stopped

        case GameActor.GameStageCommand(command) =>
            gameStageActor ! command
            Behaviors.same
    }
}

object GameActor {

    sealed trait Command

    case object Exit extends Command

    final case class GameStageCommand(command: GameStageActor.Command) extends Command

    def apply(eventService: EventService, screen: Screen, keyEventService: KeyEventService): Behavior[Command] = Behaviors.setup { context =>

        val gameStageActor = context.spawn(GameStageActor(eventService, screen, keyEventService), "GameStageActor")

        new GameActor(gameStageActor).behavior()
    }
}
