package io.github.hejcz.river.tiles

import io.github.hejcz.basic.tiles.*
import io.github.hejcz.core.*

class RiverTileRotated180(private val tile: RiverTile) : TileRotated180(tile),
    RiverTile {
    override fun exploreRiver(): Directions = tile.exploreRiver().map { it.right().right() }
}
