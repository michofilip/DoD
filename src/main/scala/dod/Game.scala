package dod


import akka.actor.typed.ActorSystem
import dod.actor.{GameActor, GameStageActor}
import dod.game.GameStage
import dod.game.event.{Event, EventProcessor}
import dod.game.gameobject.commons.{CommonsAccessor, CommonsProperty}
import dod.game.gameobject.graphics.*
import dod.game.gameobject.physics.*
import dod.game.gameobject.position.*
import dod.game.gameobject.{GameObject, GameObjectRepository}
import dod.game.temporal.Timer
import dod.game.temporal.Timestamps.Timestamp
import dod.ui.{PrimaryGameStage, Screen, SpriteRepository}
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.application.{JFXApp3, Platform}
import scalafx.scene.Scene
import scalafx.scene.layout.Pane
import scalafx.stage.StageStyle

import java.util.UUID
import scala.collection.immutable.Queue

object Game extends JFXApp3 {

    private val eventProcessor = EventProcessor()
    private val spriteData = SpriteRepository()
    private val screen = Screen(12 * 32, 12 * 32, 32, 32, spriteData)
    private val gameActor = ActorSystem(GameActor(eventProcessor, screen), "GameActor")
    private val primaryGameStage = PrimaryGameStage(gameActor, screen)


    override def start(): Unit = {
        stage = primaryGameStage.stage


        val id = UUID.randomUUID()

        val gameObject1 = GameObject(
            commonsProperty = CommonsProperty(id = id, name = "TestGameObject1", creationTimestamp = Timestamp(0)),
            positionProperty = Some(PositionProperty(coordinates = Coordinates(0, 0), direction = Direction.North, positionTimestamp = Timestamp(0))),
            graphicsProperty = Some(GraphicsProperty(layer = 1, tileWidth = 32, tileHeight = 32, animationSelector = AnimationSelector((None, Some(Direction.North)) -> Animation.OneFrameAnimation(Frame(2, 0, 0)))))
        )
        val gameObject2 = GameObject(
            commonsProperty = CommonsProperty(id = UUID.randomUUID(), name = "TestGameObject2", creationTimestamp = Timestamp(0)),
            positionProperty = Some(PositionProperty(coordinates = Coordinates(1, 0), direction = Direction.North, positionTimestamp = Timestamp(0))),
            physicsProperty = Some(PhysicsProperty(PhysicsSelector(None -> Physics(solid = false)))),
            graphicsProperty = Some(GraphicsProperty(layer = 1, tileWidth = 32, tileHeight = 32, animationSelector = AnimationSelector((None, Some(Direction.North)) -> Animation.OneFrameAnimation(Frame(1, 0, 0)))))
        )

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
