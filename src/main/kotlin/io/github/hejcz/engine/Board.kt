package io.github.hejcz.engine

import io.github.hejcz.placement.Position
import io.github.hejcz.tiles.basic.Tile

data class Board(val tiles: Map<Position, Tile>) {
    fun withTile(tile: Tile, position: Position) = Board(tiles + (position to tile))
}