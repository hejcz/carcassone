package io.github.hejcz.helper

import io.github.hejcz.basic.tiles.NoTile
import io.github.hejcz.basic.tiles.Tile
import io.github.hejcz.basic.tiles.TileD
import io.github.hejcz.core.RemainingTiles
import io.github.hejcz.river.tiles.TileBB6F1

class TestBasicRemainingTiles(private val tiles: List<Tile>) : RemainingTiles {
    constructor(vararg tiles: Tile) : this(tiles.toList())

    private val left: Iterator<Tile> = (TileD + tiles + NoTile).iterator()
    override fun next(): Tile = left.next()
    override fun size(): Int = tiles.size
}

class TestRiverRemainingTiles(private val tiles: List<Tile>) : RemainingTiles {
    constructor(vararg tiles: Tile) : this(tiles.toList())

    private val left: Iterator<Tile> = (TileBB6F1 + tiles + NoTile).iterator()
    override fun next(): Tile = left.next()
    override fun size(): Int = tiles.size
}

private operator fun Tile.plus(tiles: Collection<Tile>) = listOf(this) + tiles
