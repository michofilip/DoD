package dod.ui

import dod.game.gameobject.GameObject
import dod.game.gameobject.graphics.Frame
import dod.game.gameobject.position.Coordinates
import dod.game.temporal.Timestamps.Timestamp
import scalafx.scene.canvas.{Canvas, GraphicsContext}
import scalafx.scene.image.Image
import scalafx.scene.paint.Color

class Screen(val tilesHorizontal: Int, val tilesVertical: Int, val tileWidth: Double, val tileHeight: Double, images: Map[Int, Image]) {
    val canvas: Canvas = new Canvas(width = tilesHorizontal * tileWidth, height = tilesVertical * tileHeight)

    def width: Double = canvas.width.toDouble

    def height: Double = canvas.height.toDouble

    private def graphicsContext2D: GraphicsContext = canvas.graphicsContext2D

    def drawBackground(color: Color): Unit = {
        graphicsContext2D.fill = color
        graphicsContext2D.fillRect(0, 0, width, height)
    }

    def drawGrid(color: Color): Unit = {
        graphicsContext2D.stroke = color
        graphicsContext2D.lineWidth = 1

        for (x <- 0 to tilesHorizontal) {
            graphicsContext2D.strokeLine(x * tileWidth - .5, 0, x * tileWidth - .5, height)
        }

        for (y <- 0 to tilesVertical) {
            graphicsContext2D.strokeLine(0, y * tileHeight - .5, width, y * tileHeight - .5)
        }
    }


    def drawGameObjects(gameObjects: Seq[GameObject], focus: Coordinates, timestamp: Timestamp): Unit = {
        case class Sprite(x: Double, y: Double, level: Int, image: Image)

        val horizontalOffset = focus.x - tilesHorizontal / 2
        val verticalOffset = focus.y - tilesVertical / 2


        def isOnScreen(sprite: Sprite): Boolean =
            0 <= sprite.x + sprite.image.width.toDouble && sprite.x < width && 0 <= sprite.y + sprite.image.height.toDouble && sprite.y < height


        def spriteFrom(gameObject: GameObject): Option[Sprite] = {
            val sprite: Option[Sprite] = for {
                coordinates <- gameObject.positionAccessor.coordinates
                frame <- gameObject.graphicsAccessor.frame(timestamp)
                level <- gameObject.graphicsAccessor.level
                image <- images.get(1)
                //                image <- image.get(frame.spriteId)
            } yield {
                Sprite(
                    x = (coordinates.x + frame.offsetX - horizontalOffset) * tileWidth,
                    y = (coordinates.y + frame.offsetY - verticalOffset) * tileHeight,
                    level = level,
                    image = image
                )
            }

            sprite.filter(isOnScreen)
        }


        drawBackground(Color.White)
//        drawGrid(Color.Red)

        gameObjects.flatMap(spriteFrom).sortBy(_.level).foreach { sprite =>
            graphicsContext2D.drawImage(sprite.image, sprite.x, sprite.y, tileWidth, tileHeight)
        }
    }
}