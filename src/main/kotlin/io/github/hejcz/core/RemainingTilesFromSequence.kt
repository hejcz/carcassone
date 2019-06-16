package io.github.hejcz.core

import io.github.hejcz.core.tile.*

class RemainingTilesFromSequence(private val tiles: List<Tile>) : RemainingTiles {
    private val left: Iterator<Tile> = (tiles + NoTile).iterator()
    override fun next(): Tile = left.next()
    override fun size(): Int = tiles.size
}
