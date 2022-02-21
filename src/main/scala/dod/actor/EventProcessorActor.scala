package dod.actor

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import dod.actor.EventProcessorActor.Command
import dod.game.event.{Event, EventProcessor}
import dod.game.gameobject.GameObjectRepository

import scala.collection.immutable.Queue

final class EventProcessorActor private(eventProcessor: EventProcessor, gameStageActor: ActorRef[GameStageActor.Command]) {
    private def behaviors(): Behavior[Command] = Behaviors.receiveMessage {
        case EventProcessorActor.ProcessEvents(gameObjectRepository, events) =>
            eventProcessor.processEvents(gameObjectRepository, events) match
                case (gameObjectRepository, events) =>
                    gameStageActor ! GameStageActor.UpdateGameObjectRepository(gameObjectRepository)
                    if (events.nonEmpty) {
                        gameStageActor ! GameStageActor.AddEvents(events)
                    }
                    gameStageActor ! GameStageActor.ProcessEvents
                    Behaviors.same
    }
}

object EventProcessorActor {

    sealed trait Command

    private[actor] final case class ProcessEvents(gameObjectRepository: GameObjectRepository, events: Queue[Event]) extends Command

    def apply(eventProcessor: EventProcessor, gameStageActor: ActorRef[GameStageActor.Command]): Behavior[Command] = Behaviors.setup { context =>
        new EventProcessorActor(eventProcessor, gameStageActor).behaviors()
    }
}
