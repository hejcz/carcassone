package io.github.hejcz.helper

import io.github.hejcz.core.tile.NoTile
import io.github.hejcz.core.tile.Tile
import io.github.hejcz.core.tile.TileD
import io.github.hejcz.expansion.river.tiles.TileBB6F1

class TestBasicRemainingTiles(private val tiles: List<Tile>) : RemainingTiles {
    constructor(vararg tiles: Tile) : this(tiles.toList())

    override fun tiles(): List<Tile> = TileD + tiles + NoTile
}

class TestRiverRemainingTiles(private val tiles: List<Tile>) : RemainingTiles {
    constructor(vararg tiles: Tile) : this(tiles.toList())

    override fun tiles(): List<Tile> = TileBB6F1 + tiles + NoTile
}

private operator fun Tile.plus(tiles: Collection<Tile>) = listOf(this) + tiles
