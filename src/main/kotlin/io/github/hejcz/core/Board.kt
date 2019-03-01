package io.github.hejcz.core

import io.github.hejcz.basic.tiles.Tile

data class Board(val tiles: Map<Position, Tile>) {
    fun withTile(tile: Tile, position: Position) = Board(tiles + (position to tile))
}
