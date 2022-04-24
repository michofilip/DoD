package dod.actor

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}
import dod.actor.DisplayActor2.{Command, Display, SetGameObjectRepository, SetProcessing, Setup}
import dod.game.gameobject.GameObjectRepository
import dod.game.model.Coordinates
import dod.game.model.Timestamps.Timestamp
import dod.ui.Screen
import scalafx.application.Platform

import scala.concurrent.duration.DurationInt

final private class DisplayActor2 private(screen: Screen)(using context: ActorContext[Command]) {
    private def behavior(setup: Setup): Behavior[Command] = Behaviors.receiveMessage {
        case SetGameObjectRepository(gameObjectRepository) =>
            behavior(setup.copy(gameObjectRepository = gameObjectRepository))

        case SetProcessing(processing) =>
            behavior(setup.copy(processing = processing))

        case Display =>
            setup.gameObjectRepository match
                case Some(gameObjectRepository) if !setup.processing =>
                    val gameObjects = gameObjectRepository.findAll

                    val focus = gameObjectRepository
                        .findById("player")
                        .flatMap(_.position.coordinates)
                        .getOrElse(Coordinates(0, 0))

                    val timestamp = gameObjectRepository.findTimer("global_timers", "timer_1").fold(Timestamp.zero)(_.timestamp)

                    Platform.runLater {
                        screen.drawGameObjects(gameObjects, focus, timestamp)
                        context.self ! SetProcessing(false)
                    }

                    behavior(setup.copy(processing = true))

                case _ =>
                    Behaviors.same
    }
}

object DisplayActor2 {

    sealed trait Command

    private case object Display extends Command

    private[actor] final case class SetGameObjectRepository(gameObjectRepository: Option[GameObjectRepository]) extends Command

    private[actor] final case class SetProcessing(processing: Boolean) extends Command


    final private case class Setup(gameObjectRepository: Option[GameObjectRepository] = None, processing: Boolean = false)

    def apply(screen: Screen): Behavior[Command] = Behaviors.setup { context =>
        given ActorContext[Command] = context

        Behaviors.withTimers { timer =>

            timer.startTimerWithFixedDelay(Display, 33.milliseconds)

            new DisplayActor2(screen).behavior(Setup())
        }

    }
}
