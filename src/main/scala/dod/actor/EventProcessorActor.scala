package dod.actor

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import dod.actor.EventProcessorActor.Command
import dod.game.event.Event
import dod.game.gameobject.GameObjectRepository
import dod.service.event.EventService

import scala.collection.immutable.Queue
import scala.concurrent.duration.DurationInt

final class EventProcessorActor private(eventService: EventService, gameStageActor: ActorRef[GameStageActor.Command]) {
    private def behavior(): Behavior[Command] = Behaviors.receiveMessage {
        case EventProcessorActor.ProcessEvents(gameObjectRepository, events) =>
            eventService.processEvents(gameObjectRepository, events) match
                case (gameObjectRepository, events) =>
                    gameStageActor ! GameStageActor.UpdateGameObjectRepository(gameObjectRepository)
                    if (events.nonEmpty) {
                        gameStageActor ! GameStageActor.AddEvents(events)
                    }
                    Behaviors.same
    }
}

object EventProcessorActor {

    sealed trait Command

    private[actor] final case class ProcessEvents(gameObjectRepository: GameObjectRepository, events: Queue[Event]) extends Command


    def apply(eventService: EventService, gameStageActor: ActorRef[GameStageActor.Command]): Behavior[Command] =
        new EventProcessorActor(eventService, gameStageActor).behavior()
}
