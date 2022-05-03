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

import scala.collection.immutable.Queue
import scala.concurrent.duration.DurationInt
import scala.util.chaining.scalaUtilChainingOps

final class GameStageActor private(eventProcessorActor: ActorRef[EventProcessorActor.Command],
                                   displayActor: ActorRef[DisplayActor.Command],
                                   keyEventActor: ActorRef[KeyEventActor.Command])
                                  (using timer: TimerScheduler[Command]) {
    private def behavior(setup: Setup): Behavior[Command] = Behaviors.receiveMessage {
        case GameStageActor.SetProcessing(processing) =>
            setup.copy(processing = processing)
                .pipe(behavior)

        case GameStageActor.SetDisplaying(displaying) =>
            setup.copy(displaying = displaying).tap(updateDisplay)
                .pipe(behavior)

        case GameStageActor.SetGameStage(gameStage) =>
            setup.copy(gameStage = Some(gameStage))
                .tap(updateDisplay)
                .pipe(behavior)

        case GameStageActor.RemoveGameStage =>
            setup.copy(gameStage = None)
                .tap(updateDisplay)
                .pipe(behavior)

        case GameStageActor.ProcessEvents =>
            setup.gameStage.filter(_ => setup.processing && setup.events.nonEmpty).fold(Behaviors.same) { gameStage =>
                eventProcessorActor ! EventProcessorActor.ProcessEvents(gameStage.gameObjectRepository, setup.events)

                setup.copy(events = Queue.empty)
                    .pipe(behavior)
            }

        case GameStageActor.UpdateGameObjectRepository(gameObjectRepository) =>
            setup.gameStage.fold(Behaviors.same) { gameStage =>
                setup.copy(gameStage = Some(gameStage.updateGameObjectRepository(gameObjectRepository)))
                    .tap(updateDisplay)
                    .pipe(behavior)
            }

        case GameStageActor.AddEvents(events) =>
            if setup.gameStage.isDefined && setup.processing then
                setup.copy(events = setup.events ++ events)
                    .pipe(behavior)
            else
                Behaviors.same

        case GameStageActor.RemoveEvents =>
            setup.copy(events = Queue.empty)
                .pipe(behavior)

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

    final case class SetGameStage(gameState: GameStage) extends Command

    case object RemoveGameStage extends Command

    final case class AddEvents(events: Seq[Event]) extends Command

    case object RemoveEvents extends Command

    final case class ProcessKeyEvent(keyEvent: KeyEvent) extends Command

    // TODO probably can be merged with SetGameStage
    private[actor] final case class UpdateGameObjectRepository(gameObjectRepository: GameObjectRepository) extends Command

    private case object ProcessEvents extends Command


    private final case class Setup(gameStage: Option[GameStage] = None,
                                   events: Queue[Event] = Queue.empty,
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
