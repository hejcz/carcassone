package io.github.hejcz

import io.github.hejcz.engine.RemainingTiles
import io.github.hejcz.tiles.basic.NoTile
import io.github.hejcz.tiles.basic.Tile
import io.github.hejcz.tiles.basic.TileD
import io.github.hejcz.tiles.river.TileBB6F1

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
