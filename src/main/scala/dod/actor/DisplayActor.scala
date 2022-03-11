package dod.actor

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import dod.actor.DisplayActor.Command
import dod.actor.GameStageActor.Display
import dod.game.GameStage
import dod.game.event.{Event, EventProcessor}
import dod.game.gameobject.position.Coordinates
import dod.game.gameobject.{GameObject, GameObjectRepository}
import dod.game.temporal.Timestamps.Timestamp
import dod.ui.Screen
import scalafx.application.Platform

import scala.collection.immutable.Queue
import scala.concurrent.duration.DurationInt

final class DisplayActor private(screen: Screen) {
    private def behaviors(): Behavior[Command] = Behaviors.receiveMessage {
        case DisplayActor.Display(gameObjectRepository) =>
            Platform.runLater {
                screen.drawGameObjects(gameObjectRepository.findAll, Coordinates(0, 0), Timestamp.zero)
            }
            Behaviors.same
    }
}

object DisplayActor {

    sealed trait Command

    private[actor] final case class Display(gameObjectRepository: GameObjectRepository) extends Command


    def apply(screen: Screen): Behavior[Command] = Behaviors.setup { context =>
        new DisplayActor(screen).behaviors()
    }
}
