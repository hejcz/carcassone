package io.github.hejcz.core

import io.github.hejcz.basic.tiles.Tile

data class Board(val tiles: Map<io.github.hejcz.core.Position, Tile>) {
    fun withTile(tile: Tile, position: Position) = Board(tiles + (position to tile))
}
