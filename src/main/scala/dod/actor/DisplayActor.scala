package dod.actor

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import dod.actor.DisplayActor.Command
import dod.actor.GameStageActor.Display
import dod.game.GameStage
import dod.game.event.Event
import dod.game.gameobject.{GameObject, GameObjectRepository}
import dod.game.model.Coordinates
import dod.game.model.Timestamps.Timestamp
import dod.ui.Screen
import scalafx.application.Platform

import scala.collection.immutable.Queue
import scala.concurrent.duration.DurationInt

final class DisplayActor private(screen: Screen) {
    private def behaviors(): Behavior[Command] = Behaviors.receiveMessage {
        case DisplayActor.Display(gameObjectRepository) =>

            val gameObjects = gameObjectRepository.findAll

            val focus = gameObjectRepository
                .findById("player")
                .flatMap(_.position.coordinates)
                .getOrElse(Coordinates(0, 0))

            val timestamp = gameObjectRepository.findTimer("global_timers", "timer_1").fold(Timestamp.zero)(_.timestamp)

            Platform.runLater {
                screen.drawGameObjects(gameObjects, focus, timestamp)
            }

            Behaviors.same
    }
}

object DisplayActor {

    sealed trait Command

    private[actor] final case class Display(gameObjectRepository: GameObjectRepository) extends Command


    def apply(screen: Screen): Behavior[Command] = new DisplayActor(screen).behaviors()
}
