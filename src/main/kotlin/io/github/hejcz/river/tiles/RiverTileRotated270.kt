package io.github.hejcz.river.tiles

import io.github.hejcz.basic.tiles.*
import io.github.hejcz.core.*

class RiverTileRotated270(private val tile: RiverTile) : TileRotated270(tile),
    RiverTile {
    override fun exploreRiver(): Directions = tile.exploreRiver().map { it.left() }
}
