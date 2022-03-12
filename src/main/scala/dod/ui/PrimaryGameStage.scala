package dod.ui

import akka.actor.typed.ActorRef
import dod.actor.{GameActor, GameStageActor}
import scalafx.Includes.*
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.application.Platform
import scalafx.scene.Scene
import scalafx.scene.layout.Pane

class PrimaryGameStage(gameActor: ActorRef[GameActor.Command], screen: Screen) {

    def stage: PrimaryStage = new PrimaryStage {
        //            initStyle(StageStyle.Undecorated)
        title = "Game"
        resizable = false
        scene = new Scene {
            onKeyPressed = { keyEvent =>
                gameActor ! GameActor.GameStageCommand(GameStageActor.ProcessKeyEvent(keyEvent))
            }

            root = new Pane {
                children = screen.canvas
            }
        }
        onCloseRequest = { _ =>
            gameActor ! GameActor.Exit
            Platform.exit()
        }
    }

}
