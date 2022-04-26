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
import scala.util.chaining.scalaUtilChainingOps

final class GameStageActor private(eventProcessorActor: ActorRef[EventProcessorActor.Command],
                                   displayActor: ActorRef[DisplayActor.Command],
                                   keyEventActor: ActorRef[KeyEventActor.Command])
                                  (using timer: TimerScheduler[Command]) {
    private def behavior(setup: Setup): Behavior[Command] = Behaviors.receiveMessage {
        case GameStageActor.SetProcessing(processing) =>
            setup.copy(processing = processing).pipe(behavior)

        case GameStageActor.SetDisplaying(displaying) =>
            setup.copy(displaying = displaying).tap(updateDisplay).pipe(behavior)

        case GameStageActor.SetGameState(gameStage) =>
            setup.copy(gameStage = gameStage).tap(updateDisplay).pipe(behavior)

        case GameStageActor.ProcessEvents =>
            setup.gameStage.filter(_ => setup.processing).filter(_.events.nonEmpty).fold(Behaviors.same) { gameStage =>
                eventProcessorActor ! EventProcessorActor.ProcessEvents(gameStage.gameObjectRepository, gameStage.events)

                setup.copy(gameStage = Some(gameStage.clearEvents())).pipe(behavior)
            }

        case GameStageActor.UpdateGameObjectRepository(gameObjectRepository) =>
            setup.gameStage.fold(Behaviors.same) { gameStage =>
                setup.copy(gameStage = Some(gameStage.updateGameObjectRepository(gameObjectRepository))).tap(updateDisplay).pipe(behavior)
            }

        case GameStageActor.AddEvents(events) =>
            setup.gameStage.fold(Behaviors.same) { gameStage =>
                setup.copy(gameStage = Some(gameStage.addEvents(events))).pipe(behavior)
            }

        case GameStageActor.ProcessKeyEvent(keyEvent) =>
            setup.gameStage.filter(_ => setup.processing).fold(Behaviors.same) { gameStage =>
                keyEventActor ! KeyEventActor.ProcessKeyEvent(gameStage.gameObjectRepository, keyEvent)
                Behaviors.same
            }

    }

    private def updateDisplay(setup: Setup): Unit = {
        displayActor ! DisplayActor.SetGameObjectRepository {
            if (setup.displaying)
                setup.gameStage.map(_.gameObjectRepository)
            else
                None
        }
    }
}

object GameStageActor {

    sealed trait Command

    final case class SetProcessing(processing: Boolean) extends Command

    final case class SetDisplaying(displaying: Boolean) extends Command

    final case class SetGameState(gameState: Option[GameStage]) extends Command

    final case class AddEvents(events: Seq[Event]) extends Command

    final case class ProcessKeyEvent(keyEvent: KeyEvent) extends Command

    private[actor] final case class UpdateGameObjectRepository(gameObjectRepository: GameObjectRepository) extends Command

    private case object ProcessEvents extends Command


    private final case class Setup(gameStage: Option[GameStage] = None,
                                   processing: Boolean = false,
                                   displaying: Boolean = false)

    def apply(eventService: EventService, screen: Screen, keyEventService: KeyEventService): Behavior[Command] = Behaviors.setup { context =>
        Behaviors.withTimers { timer =>
            given TimerScheduler[Command] = timer

            val eventProcessorActor = context.spawn(EventProcessorActor(eventService, context.self), "EventProcessorActor")
            val displayActor = context.spawn(DisplayActor(screen), "DisplayActor")
            val keyEventActor = context.spawn(KeyEventActor(keyEventService, context.self), "KeyEventActor")

            timer.startTimerAtFixedRate(ProcessEvents, 2000.milliseconds, 33.milliseconds)

            new GameStageActor(eventProcessorActor, displayActor, keyEventActor).behavior(Setup())
        }
    }
}
