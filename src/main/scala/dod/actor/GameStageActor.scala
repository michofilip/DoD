package dod.actor

import akka.actor.typed.scaladsl.{ActorContext, Behaviors, TimerScheduler}
import akka.actor.typed.{ActorRef, Behavior}
import dod.actor.GameStageActor.{Command, Setup}
import dod.game.GameStage
import dod.game.event.Event
import dod.game.gameobject.GameObjectRepository
import dod.service.KeyEventService
import dod.service.event.EventService
import dod.ui.Screen
import scalafx.scene.input.KeyEvent

import scala.concurrent.duration.DurationInt

final class GameStageActor private(eventProcessorActor: ActorRef[EventProcessorActor.Command],
                                   displayActor: ActorRef[DisplayActor.Command],
                                   keyEventActor: ActorRef[KeyEventActor.Command]) {
    private def behavior(setup: Setup): Behavior[Command] = Behaviors.receiveMessage {
        case GameStageActor.SetProcessing(processing) =>
            behavior(setup.copy(processing = processing))

        case GameStageActor.SetDisplaying(displaying) =>
            behavior(setup.copy(displaying = displaying))

        case GameStageActor.SetGameState(gameStage) =>
            behavior(setup.copy(gameStage = gameStage))

        case GameStageActor.ProcessEvents =>
            setup.gameStage.filter(_ => setup.processing).filter(_.events.nonEmpty).fold(Behaviors.same) { gameStage =>
                eventProcessorActor ! EventProcessorActor.ProcessEvents(gameStage.gameObjectRepository, gameStage.events)
                behavior(setup.copy(gameStage = Some(gameStage.clearEvents())))
            }

        case GameStageActor.Display =>
            setup.gameStage.filter(_ => setup.displaying).fold(Behaviors.same) { gameStage =>
                displayActor ! DisplayActor.Display(gameStage.gameObjectRepository)
                Behaviors.same
            }

        case GameStageActor.UpdateGameObjectRepository(gameObjectRepository) =>
            setup.gameStage.fold(Behaviors.same) { gameStage =>
                behavior(setup.copy(gameStage = Some(gameStage.updateGameObjectRepository(gameObjectRepository))))
            }

        case GameStageActor.AddEvents(events) =>
            setup.gameStage.fold(Behaviors.same) { gameStage =>
                behavior(setup.copy(gameStage = Some(gameStage.addEvents(events))))
            }

        case GameStageActor.ProcessKeyEvent(keyEvent) =>
            setup.gameStage.filter(_ => setup.processing).fold(Behaviors.same) { gameStage =>
                keyEventActor ! KeyEventActor.ProcessKeyEvent(gameStage.gameObjectRepository, keyEvent)
                Behaviors.same
            }


    }
}

object GameStageActor {

    sealed trait Command

    final case class SetProcessing(processing: Boolean) extends Command

    final case class SetDisplaying(displaying: Boolean) extends Command

    final case class SetGameState(gameState: Option[GameStage]) extends Command

    private case object ProcessEvents extends Command

    private case object Display extends Command

    private[actor] final case class UpdateGameObjectRepository(gameObjectRepository: GameObjectRepository) extends Command

    final case class AddEvents(events: Seq[Event]) extends Command

    final case class ProcessKeyEvent(keyEvent: KeyEvent) extends Command


    private final case class Setup(gameStage: Option[GameStage], processing: Boolean, displaying: Boolean)


    def apply(eventService: EventService, screen: Screen, keyEventService: KeyEventService): Behavior[Command] = Behaviors.setup { context =>
        Behaviors.withTimers { timer =>
            val setup = Setup(gameStage = None, processing = false, displaying = false)

            val eventProcessorActor = context.spawn(EventProcessorActor(eventService, context.self), "EventProcessorActor")
            val displayActor = context.spawn(DisplayActor(screen), "DisplayActor")
            val keyEventActor = context.spawn(KeyEventActor(keyEventService, context.self), "KeyEventActor")

            timer.startTimerAtFixedRate(ProcessEvents, 2000.milliseconds, 33.milliseconds)
            timer.startTimerAtFixedRate(Display, 1000.milliseconds, 33.milliseconds)

            new GameStageActor(eventProcessorActor, displayActor, keyEventActor).behavior(setup)
        }
    }
}
