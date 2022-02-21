package dod.ui

import dod.utils.FileUtils
import scalafx.scene.image.{Image, WritableImage}

import java.io.{File, FileInputStream}
import scala.compiletime.ops.string

class SpriteData {

    val tilesetByName: Map[String, Image] = {
        FileUtils.filesInDir("assets", Seq("png")).map { tileSet =>
            FileUtils.fileName(tileSet) -> new Image(new FileInputStream(tileSet))
        }.toMap
    }

    case class SpriteData(id: Int, name: String, tileset: String, x: Int, y: Int, width: Int, height: Int)

    val spriteDataSeq: Seq[SpriteData] = Seq(
        SpriteData(id = 1, name = "sprite1", tileset = "tileset_32_01", x = 0, y = 0, width = 32, height = 32),
        SpriteData(id = 2, name = "sprite2", tileset = "tileset_32_01", x = 32, y = 0, width = 32, height = 32),
        SpriteData(id = 3, name = "sprite3", tileset = "tileset_32_01", x = 64, y = 0, width = 32, height = 32),
        SpriteData(id = 4, name = "sprite4", tileset = "tileset_32_01", x = 96, y = 0, width = 32, height = 32)
    )

    val sprites: Map[Int, Image] = spriteDataSeq.flatMap { spriteData =>
        tilesetByName.get(spriteData.tileset).flatMap(_.pixelReader).map { pixelReader =>
            spriteData.id -> new WritableImage(pixelReader, spriteData.x, spriteData.y, spriteData.width, spriteData.height)
        }
    }.toMap


}
