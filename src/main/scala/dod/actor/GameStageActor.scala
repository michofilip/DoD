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
                                   keyEventActor: ActorRef[KeyEventActor.Command])
                                  (using timer: TimerScheduler[Command]) {
    private def behavior(setup: Setup): Behavior[Command] = Behaviors.receiveMessage {
        case SetProcessing(processing) =>
            setup.copy(processing = processing)
                .pipe(behavior)

        case SetDisplaying(displaying) =>
            setup.copy(displaying = displaying)
                .tap(updateDisplay)
                .pipe(behavior)

        case SetGameStage(gameStage) =>
            setup.copy(gameStage = Some(gameStage))
                .tap(updateDisplay)
                .pipe(behavior)

        case RemoveGameStage =>
            setup.copy(gameStage = None, events = Queue.empty)
                .tap(updateDisplay)
                .pipe(behavior)

        case ProcessEvents =>
            setup.gameStage match
                case Some(gameStage) if setup.processing && setup.events.nonEmpty =>
                    eventProcessorActor ! EventProcessorActor.ProcessEvents(gameStage, setup.events)

                    setup.copy(events = Queue.empty)
                        .pipe(behavior)

                case _ =>
                    Behaviors.same

        case AddEvents(events) =>
            if setup.gameStage.isDefined && setup.processing then
                setup.copy(events = setup.events ++ events)
                    .pipe(behavior)
            else
                Behaviors.same

        case RemoveEvents =>
            setup.copy(events = Queue.empty)
                .pipe(behavior)

        case ProcessKeyEvent(keyEvent) =>
            setup.gameStage match
                case Some(gameStage) if setup.processing =>
                    keyEventActor ! KeyEventActor.ProcessKeyEvent(gameStage.gameObjects, keyEvent)
                    Behaviors.same

                case _ =>
                    Behaviors.same
    }

    private def updateDisplay(setup: Setup): Unit = {
        displayActor ! DisplayActor.SetGameObjectRepository {
            if (setup.displaying)
                setup.gameStage.map(_.gameObjects)
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
