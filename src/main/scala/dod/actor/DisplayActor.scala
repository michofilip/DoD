package dod.actor

import akka.actor.typed.scaladsl.{ActorContext, Behaviors, TimerScheduler}
import akka.actor.typed.{ActorRef, Behavior}
import dod.actor.DisplayActor.{Command, Display, DisplayTimer, MarkAsReady, SetGameObjectRepository, Setup}
import dod.actor.GameStageActor.SetProcessing
import dod.game.gameobject.GameObjectRepository
import dod.game.model.Coordinates
import dod.game.model.Timestamps.Timestamp
import dod.ui.Screen
import scalafx.application.Platform

import scala.concurrent.duration.{Duration, DurationInt, FiniteDuration}

final private class DisplayActor private(screen: Screen)(using context: ActorContext[Command], timer: TimerScheduler[Command]) {
    private def behavior(setup: Setup): Behavior[Command] = Behaviors.receiveMessage {
        case SetGameObjectRepository(gameObjectRepository) =>
            if (gameObjectRepository.isDefined) {
                if (!timer.isTimerActive(DisplayTimer)) {
                    timer.startTimerWithFixedDelay(DisplayTimer, Display, setup.duration)
                }
            } else {
                timer.cancel(DisplayTimer)
            }
            behavior(setup.copy(gameObjectRepository = gameObjectRepository))

        case MarkAsReady =>
            behavior(setup.copy(processing = false))

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
                        context.self ! MarkAsReady
                    }

                    behavior(setup.copy(processing = true))

                case _ =>
                    Behaviors.same
    }
}

object DisplayActor {

    private object DisplayTimer

    sealed trait Command

    private[actor] final case class SetGameObjectRepository(gameObjectRepository: Option[GameObjectRepository]) extends Command

    private[actor] case object MarkAsReady extends Command

    private case object Display extends Command


    final private case class Setup(gameObjectRepository: Option[GameObjectRepository] = None,
                                   processing: Boolean = false,
                                   duration: FiniteDuration = 33.milliseconds)

    def apply(screen: Screen): Behavior[Command] = Behaviors.setup { context =>
        given ActorContext[Command] = context

        Behaviors.withTimers { timer =>
            given TimerScheduler[Command] = timer

            new DisplayActor(screen).behavior(Setup())
        }

    }
}
