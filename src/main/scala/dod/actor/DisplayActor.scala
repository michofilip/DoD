package dod.actor

import akka.actor.typed.scaladsl.{ActorContext, Behaviors, TimerScheduler}
import akka.actor.typed.{ActorRef, Behavior}
import dod.actor.DisplayActor.{Command, Display, MarkAsReady, SetGameStage, Setup}
import dod.game.GameStage
import dod.game.gameobject.GameObjectRepository
import dod.game.model.Coordinates
import dod.game.model.Timestamps.Timestamp
import dod.ui.Screen
import scalafx.application.Platform

import scala.concurrent.duration.{Duration, DurationInt, FiniteDuration}

final private class DisplayActor private(screen: Screen)(using context: ActorContext[Command]) {
    private def behavior(setup: Setup): Behavior[Command] = Behaviors.receiveMessage {
        case SetGameStage(gameStage) => handleSetGameStage(setup, gameStage)
        case MarkAsReady => handleMarkAsReady(setup)
        case Display => handleDisplay(setup)
    }

    private inline def handleSetGameStage(setup: Setup, gameStage: Option[GameStage]): Behavior[Command] =
        behavior(setup.copy(gameStage = gameStage))

    private inline def handleMarkAsReady(setup: Setup): Behavior[Command] =
        behavior(setup.copy(processing = false))

    private inline def handleDisplay(setup: Setup): Behavior[Command] = {
        setup.gameStage.filter(_ => !setup.processing).fold(Behaviors.same) { gameStage =>
            Platform.runLater {
                screen.drawGameStage(gameStage)
                context.self ! MarkAsReady
            }

            behavior(setup.copy(processing = true))
        }
    }
}

object DisplayActor {

    sealed trait Command

    private[actor] final case class SetGameStage(gameStage: Option[GameStage]) extends Command

    private[actor] case object MarkAsReady extends Command

    private case object Display extends Command


    final private case class Setup(gameStage: Option[GameStage] = None, processing: Boolean = false)

    def apply(screen: Screen): Behavior[Command] = Behaviors.setup { context =>
        given ActorContext[Command] = context

        Behaviors.withTimers { timer =>
            timer.startTimerWithFixedDelay(Display, 33.milliseconds)

            new DisplayActor(screen).behavior(Setup())
        }

    }
}
