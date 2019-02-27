package io.github.hejcz.tiles.river

import io.github.hejcz.placement.Direction
import io.github.hejcz.tiles.basic.TileRotated90

class RiverTileRotated90(private val tile: RiverTile) : TileRotated90(tile), RiverTile {
    override fun exploreRiver(): Collection<Direction> = tile.exploreRiver().map { it.right() }
}
