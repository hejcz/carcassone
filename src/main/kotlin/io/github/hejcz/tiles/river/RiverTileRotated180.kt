package io.github.hejcz.tiles.river

import io.github.hejcz.placement.Direction
import io.github.hejcz.tiles.basic.TileRotated180

class RiverTileRotated180(private val tile: RiverTile) : TileRotated180(tile), RiverTile {
    override fun exploreRiver(): Collection<Direction> = tile.exploreRiver().map { it.right().right() }
}
