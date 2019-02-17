package io.github.hejcz.engine

import io.github.hejcz.placement.Position
import io.github.hejcz.tiles.basic.Tile
import io.github.hejcz.tiles.basic.TileD

data class Board(val tiles: Map<Position, Tile> = mapOf(Position(0, 0) to TileD)) {
    fun withTile(tile: Tile, position: Position) = copy(tiles = tiles.plus(position to tile))
}