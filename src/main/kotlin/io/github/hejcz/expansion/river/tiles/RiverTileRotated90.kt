package io.github.hejcz.expansion.river.tiles

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*

class RiverTileRotated90(private val tile: RiverTile) : TileRotated90(tile), RiverTile {
    override fun exploreRiver(): Directions = tile.exploreRiver().map { it.right() }
}
