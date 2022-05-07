package dod.actor

import akka.actor.typed.scaladsl.{ActorContext, Behaviors, TimerScheduler}
import akka.actor.typed.{ActorRef, Behavior}
import dod.actor.DisplayActor.{Command, Display, DisplayTimer, MarkAsReady, SetGameStage, Setup}
import dod.actor.GameStageActor.SetProcessing
import dod.game.GameStage
import dod.game.gameobject.GameObjectRepository
import dod.game.model.Coordinates
import dod.game.model.Timestamps.Timestamp
import dod.ui.Screen
import scalafx.application.Platform

import scala.concurrent.duration.{Duration, DurationInt, FiniteDuration}

final private class DisplayActor private(screen: Screen)(using context: ActorContext[Command], timer: TimerScheduler[Command]) {
    private def behavior(setup: Setup): Behavior[Command] = Behaviors.receiveMessage {
        case SetGameStage(gameStage) => handleSetGameStage(setup, gameStage)
        case MarkAsReady => handleMarkAsReady(setup)
        case Display => handleDisplay(setup)
    }

    private def handleDisplay(setup: Setup): Behavior[Command] = {
        setup.gameStage match
            case Some(gameStage) if !setup.processing =>
                val gameObjects = gameStage.gameObjects.findAll

                val focus = gameStage.gameObjects
                    .findById("player")
                    .flatMap(_.position.coordinates)
                    .getOrElse(Coordinates(0, 0))

                val timestamp = gameStage.gameObjects.findTimer("global_timers", "timer_1").fold(Timestamp.zero)(_.timestamp)

                Platform.runLater {
                    screen.drawGameObjects(gameObjects, focus, timestamp)
                    context.self ! MarkAsReady
                }

                behavior(setup.copy(processing = true))

            case _ =>
                Behaviors.same
    }

    private def handleMarkAsReady(setup: Setup) =
        behavior(setup.copy(processing = false))

    private inline def handleSetGameStage(setup: Setup, gameStage: Option[GameStage]): Behavior[Command] = {
        if (gameStage.isDefined) {
            if (!timer.isTimerActive(DisplayTimer)) {
                timer.startTimerWithFixedDelay(DisplayTimer, Display, setup.duration)
            }
        } else {
            timer.cancel(DisplayTimer)
        }
        behavior(setup.copy(gameStage = gameStage))
    }
}

object DisplayActor {

    private object DisplayTimer


    sealed trait Command

    private[actor] final case class SetGameStage(gameStage: Option[GameStage]) extends Command

    private[actor] case object MarkAsReady extends Command

    private case object Display extends Command


    final private case class Setup(gameStage: Option[GameStage] = None,
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
