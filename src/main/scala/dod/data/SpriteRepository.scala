package dod.data

import dod.utils.FileUtils
import scalafx.scene.image.{Image, WritableImage}

import java.io.{File, FileInputStream}
import scala.compiletime.ops.string

class SpriteRepository(tileRepository: TileRepository, tilesetRepository: TilesetRepository) {
    val sprites: Map[Int, Image] = tileRepository.tiles.flatMap { spriteData =>
        tilesetRepository.tilesetByName.get(spriteData.tileset).flatMap(_.pixelReader).map { pixelReader =>
            spriteData.id -> new WritableImage(pixelReader, spriteData.x, spriteData.y, spriteData.width, spriteData.height)
        }
    }.toMap
}
