package io.github.hejcz.components.river.tiles

import io.github.hejcz.api.Directions
import io.github.hejcz.base.tile.TileRotated270

class RiverTileRotated270(private val tile: RiverTile) : TileRotated270(tile),
    RiverTile {
    override fun exploreRiver(): Directions = tile.exploreRiver().map { it.left() }
}
