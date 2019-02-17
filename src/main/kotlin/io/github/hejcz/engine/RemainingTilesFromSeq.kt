package io.github.hejcz.engine

import io.github.hejcz.tiles.basic.NoTile
import io.github.hejcz.tiles.basic.Tile

class RemainingTilesFromSeq(private val tiles: List<Tile>) : RemainingTiles {
    constructor(vararg tiles: Tile) : this(tiles.toList())
    private val left: Iterator<Tile> = (tiles + NoTile).iterator()
    override fun next(): Tile = left.next()
    override fun size(): Int = tiles.size
}