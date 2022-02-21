package dod.actor

import akka.actor.typed.scaladsl.{ActorContext, Behaviors, TimerScheduler}
import akka.actor.typed.{ActorRef, Behavior}
import dod.actor.GameStageActor.{Command, Setup}
import dod.game.GameStage
import dod.game.event.{Event, EventProcessor}
import dod.game.gameobject.GameObjectRepository
import dod.ui.Screen

import scala.concurrent.duration.DurationInt

final class GameStageActor private(context: ActorContext[Command],
                                   timer: TimerScheduler[Command],
                                   eventProcessorActor: ActorRef[EventProcessorActor.Command],
                                   displayActor: ActorRef[DisplayActor.Command]) {
    private def behaviors(setup: Setup): Behavior[Command] = Behaviors.receiveMessage {
        case GameStageActor.SetProcessing(processing) =>
            behaviors(setup.copy(processing = processing))

        case GameStageActor.SetDisplaying(displaying) =>
            behaviors(setup.copy(displaying = displaying))

        case GameStageActor.SetGameState(gameStage) =>
            behaviors(setup.copy(gameStage = gameStage))

        case GameStageActor.ProcessEvents =>
            setup.gameStage match
                case Some(gameStage) if setup.processing && gameStage.events.nonEmpty =>
                    eventProcessorActor ! EventProcessorActor.ProcessEvents(gameStage.gameObjectRepository, gameStage.events)
                    behaviors(setup.copy(gameStage = Some(gameStage.clearEvents())))
                case _ =>
                    timer.startSingleTimer(GameStageActor.ProcessEvents, 33.milliseconds)
                    //                    context.self ! GameStageActor.ProcessEvents
                    Behaviors.same

        case GameStageActor.Display =>
            setup.gameStage match
                case Some(gameStage) if setup.displaying =>
                    displayActor ! DisplayActor.Display(gameStage.gameObjectRepository.findAll)
                    Behaviors.same
                case _ =>
                    Behaviors.same

        case GameStageActor.UpdateGameObjectRepository(gameObjectRepository) =>
            setup.gameStage match
                case Some(gameStage) =>
                    behaviors(setup.copy(gameStage = Some(gameStage.updateGameObjectRepository(gameObjectRepository))))
                case None =>
                    Behaviors.same

        case GameStageActor.AddEvents(events) =>
            setup.gameStage match
                case Some(gameStage) =>
                    behaviors(setup.copy(gameStage = Some(gameStage.addEvents(events))))
                case None =>
                    Behaviors.same
    }
}

object GameStageActor {

    sealed trait Command

    final case class SetProcessing(processing: Boolean) extends Command

    final case class SetDisplaying(displaying: Boolean) extends Command

    final case class SetGameState(gameState: Option[GameStage]) extends Command

    private[actor] case object ProcessEvents extends Command

    private[actor] case object Display extends Command

    private[actor] final case class UpdateGameObjectRepository(gameObjectRepository: GameObjectRepository) extends Command

    final case class AddEvents(events: Seq[Event]) extends Command


    private final case class Setup(gameStage: Option[GameStage], processing: Boolean, displaying: Boolean)


    def apply(eventProcessor: EventProcessor, screen: Screen): Behavior[Command] = Behaviors.setup { context =>
        Behaviors.withTimers { timer =>
            val setup = Setup(gameStage = None, processing = false, displaying = false)

            val eventProcessorActor = context.spawn(EventProcessorActor(eventProcessor, context.self), "EventProcessorActor")
            val displayActor: ActorRef[DisplayActor.Command] = context.spawn(DisplayActor(screen, context.self), "DisplayActor")

            context.self ! ProcessEvents
            timer.startTimerAtFixedRate(Display, 33.milliseconds)

            new GameStageActor(context, timer, eventProcessorActor, displayActor).behaviors(setup)
        }
    }
}
