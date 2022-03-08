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

    private val eventProcessor = EventProcessor()
    private val tileRepository = TileRepository()
    private val tilesetRepository = TilesetRepository()
    private val spriteRepository = SpriteRepository(tileRepository, tilesetRepository)
    private val screen = Screen(11 * 32, 11 * 32, 32, 32, spriteRepository)
    private val gameActor = ActorSystem(GameActor(eventProcessor, screen), "GameActor")
    private val primaryGameStage = PrimaryGameStage(gameActor, screen)


    override def start(): Unit = {
        stage = primaryGameStage.stage


        val id = UUID.randomUUID()

        val gameObject1 = GameObject(
            commonsProperty = CommonsProperty(id = id, name = "TestGameObject1", creationTimestamp = Timestamp(0)),
            positionProperty = Some(PositionProperty(Position(coordinates = Coordinates(0, 0), direction = Direction.East), positionTimestamp = Timestamp(0))),
            graphicsProperty = Some(GraphicsProperty(animationSelector = AnimationSelector((None, Some(Direction.East)) -> Animation.OneFrameAnimation(1, Frame(6, 0, 0)))))
        )
        val gameObject2 = GameObject(
            commonsProperty = CommonsProperty(id = UUID.randomUUID(), name = "TestGameObject2", creationTimestamp = Timestamp(0)),
            positionProperty = Some(PositionProperty(Position(coordinates = Coordinates(1, 0)), positionTimestamp = Timestamp(0))),
            physicsProperty = Some(PhysicsProperty(PhysicsSelector(None -> Physics(solid = false)))),
            graphicsProperty = Some(GraphicsProperty(animationSelector = AnimationSelector((None, None) -> Animation.OneFrameAnimation(1, Frame(9, 0, 0)))))
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
