package dod.actor

import akka.actor.typed.scaladsl.{ActorContext, Behaviors, TimerScheduler}
import akka.actor.typed.{ActorRef, Behavior}
import dod.actor.GameStageActor.*
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
                                   keyEventActor: ActorRef[KeyEventActor.Command]) {
    private def behavior(setup: Setup): Behavior[Command] = Behaviors.receiveMessage {
        case SetProcessingEnabled(processingEnabled) => handleSetProcessingEnabled(setup, processingEnabled)
        case SetDisplayingEnabled(displayingEnabled) => handleSetDisplayingEnabled(setup, displayingEnabled)
        case SetGameStage(gameStage) => handleSetGameStage(setup, gameStage)
        case RemoveGameStage => handleRemoveGameStage(setup)
        case ProcessEvents => handleProcessEvents(setup)
        case MarkAsReady => handleMarkAsReady(setup)
        case AddEvents(events) => handleAddEvents(setup, events)
        case RemoveEvents => handleRemoveEvents(setup)
        case ProcessKeyEvent(keyEvent) => handleProcessKeyEvent(setup, keyEvent)
    }

    private inline def handleSetProcessingEnabled(setup: Setup, processingEnabled: Boolean): Behavior[Command] =
        setup.copy(processingEnabled = processingEnabled)
            .pipe(behavior)

    private inline def handleSetDisplayingEnabled(setup: Setup, displayingEnabled: Boolean): Behavior[Command] =
        setup.copy(displayingEnabled = displayingEnabled)
            .tap(updateDisplay)
            .pipe(behavior)

    private inline def handleSetGameStage(setup: Setup, gameStage: GameStage): Behavior[Command] =
        setup.copy(gameStage = Some(gameStage))
            .tap(updateDisplay)
            .pipe(behavior)

    private inline def handleRemoveGameStage(setup: Setup): Behavior[Command] =
        setup.copy(gameStage = None, events = Queue.empty)
            .tap(updateDisplay)
            .pipe(behavior)

    private inline def handleProcessEvents(setup: Setup): Behavior[Command] =
        setup.gameStage.filter(_ => setup.processingEnabled && !setup.processing && setup.events.nonEmpty).fold(Behaviors.same) { gameStage =>
            eventProcessorActor ! EventProcessorActor.ProcessEvents(gameStage, setup.events)

            setup.copy(events = Queue.empty, processing = true)
                .pipe(behavior)
        }

    private inline def handleMarkAsReady(setup: Setup): Behavior[Command] =
        setup.copy(processing = false)
            .pipe(behavior)

    private inline def handleAddEvents(setup: Setup, events: Seq[Event]): Behavior[Command] =
        setup.gameStage.filter(_ => setup.processingEnabled).fold(Behaviors.same) { _ =>
            setup.copy(events = setup.events ++ events)
                .pipe(behavior)
        }

    private inline def handleRemoveEvents(setup: Setup): Behavior[Command] =
        setup.copy(events = Queue.empty)
            .pipe(behavior)

    private inline def handleProcessKeyEvent(setup: Setup, keyEvent: KeyEvent): Behavior[Command] =
        setup.gameStage.filter(_ => setup.processingEnabled).fold(Behaviors.same) { gameStage =>
            keyEventActor ! KeyEventActor.ProcessKeyEvent(gameStage, keyEvent)
            Behaviors.same
        }

    private def updateDisplay(setup: Setup): Unit =
        displayActor ! DisplayActor.SetGameStage {
            if setup.displayingEnabled then setup.gameStage else None
        }
}

object GameStageActor {

    sealed trait Command

    final case class SetProcessingEnabled(processingEnabled: Boolean) extends Command

    final case class SetDisplayingEnabled(displayingEnabled: Boolean) extends Command

    final case class SetGameStage(gameState: GameStage) extends Command

    case object RemoveGameStage extends Command

    final case class AddEvents(events: Seq[Event]) extends Command

    case object RemoveEvents extends Command

    final case class ProcessKeyEvent(keyEvent: KeyEvent) extends Command

    private[actor] case object MarkAsReady extends Command

    private case object ProcessEvents extends Command


    private final case class Setup(gameStage: Option[GameStage] = None,
                                   events: Queue[Event] = Queue.empty,
                                   processingEnabled: Boolean = false,
                                   displayingEnabled: Boolean = false,
                                   processing: Boolean = false)

    def apply(eventService: EventService, screen: Screen, keyEventService: KeyEventService): Behavior[Command] = Behaviors.setup { context =>
        Behaviors.withTimers { timer =>
            val eventProcessorActor = context.spawn(EventProcessorActor(eventService, context.self), "EventProcessorActor")
            val displayActor = context.spawn(DisplayActor(screen), "DisplayActor")
            val keyEventActor = context.spawn(KeyEventActor(keyEventService, context.self), "KeyEventActor")

            timer.startTimerAtFixedRate(ProcessEvents, 10.milliseconds)

            new GameStageActor(eventProcessorActor, displayActor, keyEventActor).behavior(Setup())
        }
    }
}
