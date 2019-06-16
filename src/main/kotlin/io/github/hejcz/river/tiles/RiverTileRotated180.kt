package io.github.hejcz.river.tiles

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*

class RiverTileRotated180(private val tile: RiverTile) : TileRotated180(tile),
    RiverTile {
    override fun exploreRiver(): Directions = tile.exploreRiver().map { it.right().right() }
}
