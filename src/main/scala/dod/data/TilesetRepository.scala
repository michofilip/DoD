package dod.data

import dod.utils.FileUtils
import scalafx.scene.image.Image

import java.io.FileInputStream

class TilesetRepository {
    val tilesetByName: Map[String, Image] = {
        FileUtils.filesInDir("assets", Seq("png")).map { tileSet =>
            FileUtils.fileName(tileSet) -> new Image(new FileInputStream(tileSet))
        }.toMap
    }
}
