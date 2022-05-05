package dod.actor

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import dod.actor.EventProcessorActor.Command
import dod.game.GameStage
import dod.game.event.Event
import dod.game.gameobject.GameObjectRepository
import dod.service.event.EventService

import scala.collection.immutable.Queue
import scala.concurrent.duration.DurationInt
import scala.util.chaining.scalaUtilChainingOps

final class EventProcessorActor private(eventService: EventService, gameStageActor: ActorRef[GameStageActor.Command]) {
    private def behavior(): Behavior[Command] = Behaviors.receiveMessage {
        case EventProcessorActor.ProcessEvents(gameStage, events) =>
            eventService.processEvents(gameStage, events).pipe { case (gameStage, events) =>
                gameStageActor ! GameStageActor.SetGameStage(gameStage)
                if (events.nonEmpty) {
                    gameStageActor ! GameStageActor.AddEvents(events)
                }
            }
            Behaviors.same
    }
}

object EventProcessorActor {

    sealed trait Command

    private[actor] final case class ProcessEvents(gameStage: GameStage, events: Queue[Event]) extends Command


    def apply(eventService: EventService, gameStageActor: ActorRef[GameStageActor.Command]): Behavior[Command] =
        new EventProcessorActor(eventService, gameStageActor).behavior()
}
