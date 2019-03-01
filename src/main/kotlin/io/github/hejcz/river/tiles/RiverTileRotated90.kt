package io.github.hejcz.river.tiles

import io.github.hejcz.basic.tiles.*
import io.github.hejcz.core.*

class RiverTileRotated90(private val tile: RiverTile) : TileRotated90(tile), RiverTile {
    override fun exploreRiver(): Directions = tile.exploreRiver().map { it.right() }
}
