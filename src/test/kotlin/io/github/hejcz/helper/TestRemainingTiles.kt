package io.github.hejcz.helper

import io.github.hejcz.core.tile.*
import io.github.hejcz.river.tiles.*

class TestBasicRemainingTiles(private val tiles: List<Tile>) : RemainingTiles {
    constructor(vararg tiles: Tile) : this(tiles.toList())

    override fun tiles(): List<Tile> = TileD + tiles + NoTile
}

class TestRiverRemainingTiles(private val tiles: List<Tile>) : RemainingTiles {
    constructor(vararg tiles: Tile) : this(tiles.toList())

    override fun tiles(): List<Tile> = TileBB6F1 + tiles + NoTile
}

private operator fun Tile.plus(tiles: Collection<Tile>) = listOf(this) + tiles
