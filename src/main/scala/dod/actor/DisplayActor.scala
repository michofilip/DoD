package dod.actor

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import dod.actor.DisplayActor.Command
import dod.game.GameStage
import dod.game.event.{Event, EventProcessor}
import dod.game.gameobject.{GameObject, GameObjectRepository}

import scala.collection.immutable.Queue

final class DisplayActor private() {
    private def behaviors(): Behavior[Command] = Behaviors.receiveMessage {
        case DisplayActor.Display(gameObjects) =>
            println(gameObjects.mkString("\n"))
            println()
            Behaviors.same
    }
}

object DisplayActor {

    sealed trait Command

    private[actor] final case class Display(gameObjects: Seq[GameObject]) extends Command

    def apply(): Behavior[Command] = Behaviors.setup { context =>
        new DisplayActor().behaviors()
    }
}
