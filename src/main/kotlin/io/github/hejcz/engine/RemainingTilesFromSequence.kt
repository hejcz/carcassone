package io.github.hejcz.engine

import io.github.hejcz.tiles.basic.NoTile
import io.github.hejcz.tiles.basic.Tile

class RemainingTilesFromSequence(private val tiles: List<Tile>) : RemainingTiles {
    private val left: Iterator<Tile> = (tiles + NoTile).iterator()
    override fun next(): Tile = left.next()
    override fun size(): Int = tiles.size
}