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

import java.util.UUID
import scala.collection.immutable.Queue
import scala.util.chaining.scalaUtilChainingOps

object Main {
    def main(args: Array[String]): Unit = {

        given EventProcessor = new EventProcessor()

        val id = UUID.randomUUID()

        val gameObject1 = GameObject(
            commonsProperty = CommonsProperty(id = id, name = "TestGameObject1", creationTimestamp = Timestamp(0)),
            positionProperty = Some(PositionProperty(coordinates = Coordinates(0, 0), direction = Direction.North, positionTimestamp = Timestamp(0))),
            graphicsProperty = Some(GraphicsProperty(layer = 1, tileWidth = 32, tileHeight = 32, animationSelector = AnimationSelector((None, Some(Direction.North)) -> Animation.OneFrameAnimation(Frame(1, 0, 0)))))
        )
        val gameObject2 = GameObject(
            commonsProperty = CommonsProperty(id = UUID.randomUUID(), name = "TestGameObject2", creationTimestamp = Timestamp(0)),
            positionProperty = Some(PositionProperty(coordinates = Coordinates(1, 0), direction = Direction.North, positionTimestamp = Timestamp(0))),
            physicsProperty = Some(PhysicsProperty(PhysicsSelector(None -> Physics(solid = false)))),
            graphicsProperty = Some(GraphicsProperty(layer = 1, tileWidth = 32, tileHeight = 32, animationSelector = AnimationSelector((None, Some(Direction.North)) -> Animation.OneFrameAnimation(Frame(2, 0, 0)))))
        )

        val gameObjectRepository = GameObjectRepository(Seq(gameObject1, gameObject2))

        val gameState = GameStage(
            gameObjectRepository = gameObjectRepository,
            events = Queue(Event.MoveBy(id, Shift(1, 0)))
        )

        //        val gameActor = ActorSystem(GameActor(new EventProcessor()), "GameActor")
        //
        //        gameActor ! GameActor.GameStageCommand(GameStageActor.SetGameState(Some(gameState)))
        //        gameActor ! GameActor.GameStageCommand(GameStageActor.SetDisplaying(true))
        //        Thread.sleep(2000)
        //        gameActor ! GameActor.GameStageCommand(GameStageActor.SetProcessing(true))
        //
        //        Thread.sleep(5000)
        //        gameActor ! GameActor.Exit


    }

}
