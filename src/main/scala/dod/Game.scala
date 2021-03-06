package dod


import akka.actor.typed.ActorSystem
import dod.actor.{GameActor, GameStageActor}
import dod.data.{SpriteRepository, TileRepository, TilesetRepository}
import dod.game.GameStage
import dod.game.event.Event
import dod.game.gameobject.commons.CommonsProperty
import dod.game.gameobject.graphics.*
import dod.game.gameobject.physics.*
import dod.game.gameobject.position.*
import dod.game.gameobject.{GameObject, GameObjectRepository}
import dod.game.model.{Timer, Timestamp}
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
    private val screen = Screen(11 * 32, 11 * 32, 32, 32, context.spriteRepository)
    private val gameActor = ActorSystem(GameActor(context.eventService, screen, context.keyEventService), "GameActor")
    private val primaryGameStage = PrimaryGameStage(gameActor, screen)


    override def start(): Unit = {
        stage = primaryGameStage.stage

        //        val (gameStage, events) = context.gameStageService.getGameStageTest
        val (gameStage, events) = context.gameStageService.getGameStageFomMap("map1")

        gameActor ! GameActor.GameStageCommand(GameStageActor.SetGameStage(gameStage))
        gameActor ! GameActor.GameStageCommand(GameStageActor.AddEvents(events))
        gameActor ! GameActor.GameStageCommand(GameStageActor.SetDisplayingEnabled(true))
        gameActor ! GameActor.GameStageCommand(GameStageActor.SetProcessingEnabled(true))
    }

}
