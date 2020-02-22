package io.github.hejcz.components.river.tiles

import io.github.hejcz.api.Directions
import io.github.hejcz.base.tile.TileRotated90

class RiverTileRotated90(private val tile: RiverTile) : TileRotated90(tile), RiverTile {
    override fun exploreRiver(): Directions = tile.exploreRiver().map { it.right() }
}
