package dod.data

class TileRepository {
    case class Tile(id: Int, name: String, tileset: String, x: Int, y: Int, width: Int, height: Int)

    val tiles: Seq[Tile] = Seq(
        Tile(id = 1, name = "empty", tileset = "tileset_32_01", x = 32 * 0, y = 32 * 0, width = 32, height = 32),
        Tile(id = 2, name = "error", tileset = "tileset_32_01", x = 32 * 1, y = 32 * 0, width = 32, height = 32),
        Tile(id = 3, name = "floor", tileset = "tileset_32_01", x = 32 * 2, y = 32 * 0, width = 32, height = 32),
        Tile(id = 4, name = "wall", tileset = "tileset_32_01", x = 32 * 3, y = 32 * 0, width = 32, height = 32),
        Tile(id = 5, name = "player_north", tileset = "tileset_32_01", x = 32 * 0, y = 32 * 1, width = 32, height = 32),
        Tile(id = 6, name = "player_east", tileset = "tileset_32_01", x = 32 * 1, y = 32 * 1, width = 32, height = 32),
        Tile(id = 7, name = "player_south", tileset = "tileset_32_01", x = 32 * 2, y = 32 * 1, width = 32, height = 32),
        Tile(id = 8, name = "player_west", tileset = "tileset_32_01", x = 32 * 3, y = 32 * 1, width = 32, height = 32),
        Tile(id = 9, name = "door_open", tileset = "tileset_32_01", x = 32 * 0, y = 32 * 2, width = 32, height = 32),
        Tile(id = 10, name = "door_closed", tileset = "tileset_32_01", x = 32 * 1, y = 32 * 2, width = 32, height = 32),
        Tile(id = 11, name = "switch_off", tileset = "tileset_32_01", x = 32 * 2, y = 32 * 2, width = 32, height = 32),
        Tile(id = 12, name = "switch_on", tileset = "tileset_32_01", x = 32 * 3, y = 32 * 2, width = 32, height = 32)
    )
}
