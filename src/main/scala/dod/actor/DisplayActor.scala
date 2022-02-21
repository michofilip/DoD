package dod.actor

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import dod.actor.DisplayActor.Command
import dod.game.GameStage
import dod.game.event.{Event, EventProcessor}
import dod.game.gameobject.position.Coordinates
import dod.game.gameobject.{GameObject, GameObjectRepository}
import dod.game.temporal.Timestamps.Timestamp
import dod.ui.Screen

import scala.collection.immutable.Queue

final class DisplayActor private(screen: Screen, gameStageActor: ActorRef[GameStageActor.Command]) {
    private def behaviors(): Behavior[Command] = Behaviors.receiveMessage {
        case DisplayActor.Display(gameObjects) =>
            screen.drawGameObjects(gameObjects, Coordinates(0, 0), Timestamp.zero)
            Behaviors.same
    }
}

object DisplayActor {

    sealed trait Command

    private[actor] final case class Display(gameObjects: Seq[GameObject]) extends Command

    def apply(screen: Screen, gameStageActor: ActorRef[GameStageActor.Command]): Behavior[Command] = Behaviors.setup { context =>
        new DisplayActor(screen, gameStageActor).behaviors()
    }
}
