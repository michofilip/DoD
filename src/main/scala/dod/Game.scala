package dod


import akka.actor.typed.ActorSystem
import dod.actor.{GameActor, GameStageActor}
import dod.data.{SpriteRepository, TileRepository, TilesetRepository}
import dod.game.GameStage
import dod.game.event.{Event, EventProcessor}
import dod.game.gameobject.commons.{CommonsAccessor, CommonsProperty}
import dod.game.gameobject.graphics.*
import dod.game.gameobject.physics.*
import dod.game.gameobject.position.*
import dod.game.gameobject.{GameObject, GameObjectRepository}
import dod.game.temporal.Timer
import dod.game.temporal.Timestamps.Timestamp
import dod.ui.{PrimaryGameStage, Screen}
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.application.{JFXApp3, Platform}
import scalafx.scene.Scene
import scalafx.scene.layout.Pane
import scalafx.stage.StageStyle

import java.util.UUID
import scala.collection.immutable.Queue

object Game extends JFXApp3 {

    private val context = new Context
    private val eventProcessor = EventProcessor()
    private val screen = Screen(11 * 32, 11 * 32, 32, 32, context.spriteRepository)
    private val gameActor = ActorSystem(GameActor(eventProcessor, screen), "GameActor")
    private val primaryGameStage = PrimaryGameStage(gameActor, screen)


    override def start(): Unit = {
        stage = primaryGameStage.stage


        val id = UUID.randomUUID()

        val gameObject1 = context.gameObjectService.createPlayer(id, Timestamp.zero, Coordinates(0, 0), Direction.East)
        val gameObject2 = context.gameObjectService.createDoor(UUID.randomUUID(), Timestamp.zero, Coordinates(1, 0), closed = false)

        val gameObjectRepository = GameObjectRepository(Seq(gameObject1, gameObject2))
        val events = Queue(Event.MoveBy(id, Shift(1, 0)))

        val gameState = GameStage(
            gameObjectRepository = gameObjectRepository,
            events = events
        )


        gameActor ! GameActor.GameStageCommand(GameStageActor.SetGameState(Some(gameState)))
        gameActor ! GameActor.GameStageCommand(GameStageActor.SetDisplaying(true))
        gameActor ! GameActor.GameStageCommand(GameStageActor.SetProcessing(true))
    }

}
