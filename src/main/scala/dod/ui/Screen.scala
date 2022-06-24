package dod.ui

import dod.data.SpriteRepository
import dod.game.GameStage
import dod.game.gameobject.GameObject
import dod.game.model.{Coordinates, Frame, Timestamp}
import dod.ui.Screen.Sprite
import scalafx.scene.canvas.{Canvas, GraphicsContext}
import scalafx.scene.image.Image
import scalafx.scene.paint.Color

class Screen(width: Double, height: Double, val tileWidth: Double, val tileHeight: Double, spriteRepository: SpriteRepository) {
    val canvas: Canvas = new Canvas(width, height)

    private val graphicsContext2D: GraphicsContext = canvas.graphicsContext2D

    def drawGameStage(gameStage: GameStage, litCoordinates: Option[Set[Coordinates]] = None): Unit = {
        val gameObjects = gameStage.gameObjects.findAll
        val focus = gameStage.focus
        val timestamp = gameStage.globalTimestamp

        drawGameObjects(gameObjects, focus, timestamp, litCoordinates)
    }

    private inline def drawBackground(color: Color): Unit = {
        graphicsContext2D.fill = color
        graphicsContext2D.fillRect(0, 0, width, height)
    }

    private inline def drawGrid(color: Color): Unit = {
        graphicsContext2D.stroke = color
        graphicsContext2D.lineWidth = 1

        val tw = tileWidth / 2
        val w = width / 2
        val dw = ((w - tw) / tileWidth).toInt

        val th = tileHeight / 2
        val h = height / 2
        val dh = ((h - th) / tileHeight).toInt

        for (x <- 0 to dw) {
            val x1 = w - tw - x * tileWidth + .5
            val x2 = w + tw + x * tileWidth - .5
            graphicsContext2D.strokeLine(x1, 0, x1, height)
            graphicsContext2D.strokeLine(x2, 0, x2, height)
        }

        for (y <- 0 to dh) {
            val y1 = h - th - y * tileHeight + .5
            val y2 = h + th + y * tileHeight - .5
            graphicsContext2D.strokeLine(0, y1, width, y1)
            graphicsContext2D.strokeLine(0, y2, width, y2)
        }
    }


    private inline def drawGameObjects(gameObjects: Seq[GameObject], focus: Coordinates, timestamp: Timestamp, litCoordinates: Option[Set[Coordinates]]): Unit = {
        val offsetX = focus.x * tileWidth - (width - tileWidth) / 2
        val offsetY = focus.y * tileHeight - (height - tileHeight) / 2

        // TODO move to some settings
        val frameTileWidth = 32
        val frameTileHeight = 32

        val scaleX = tileWidth / frameTileWidth
        val scaleY = tileHeight / frameTileHeight

        def isLit(coordinates: Coordinates): Boolean = litCoordinates.fold(true)(_.contains(coordinates))

        def spriteFrom(gameObject: GameObject): Option[Sprite] = for
            coordinates <- gameObject.position.coordinates

            // FIXME this should be shadow, not filtered out
            if isLit(coordinates)

            frame <- gameObject.graphics.frame(timestamp)
            layer <- gameObject.graphics.layer
            image <- spriteRepository.sprites.get(frame.spriteId)

            x = (coordinates.x + frame.offsetX) * tileWidth - offsetX
            y = (coordinates.y + frame.offsetY) * tileHeight - offsetY
            scaledWidth = image.width.toDouble * scaleX
            scaledHeight = image.height.toDouble * scaleY

            if 0 <= x + scaledWidth && x < width && 0 <= y + scaledHeight && y < height
        yield Sprite(x, y, scaledWidth, scaledHeight, layer, image)


        drawBackground(Color.LightGray)

        gameObjects.flatMap(spriteFrom).sorted.foreach { sprite =>
            graphicsContext2D.drawImage(sprite.image, sprite.x, sprite.y, sprite.width, sprite.height)
        }

        drawGrid(Color.Red)
    }
}

object Screen {
    case class Sprite(x: Double, y: Double, width: Double, height: Double, layer: Int, image: Image)

    given Ordering[Sprite] = Ordering.by(sprite => (-sprite.y, sprite.layer))
}
