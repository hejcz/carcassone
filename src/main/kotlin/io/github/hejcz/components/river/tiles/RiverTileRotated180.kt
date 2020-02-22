package io.github.hejcz.components.river.tiles

import io.github.hejcz.api.Directions
import io.github.hejcz.base.tile.TileRotated180

class RiverTileRotated180(private val tile: RiverTile) : TileRotated180(tile),
    RiverTile {
    override fun exploreRiver(): Directions = tile.exploreRiver().map { it.right().right() }
}
