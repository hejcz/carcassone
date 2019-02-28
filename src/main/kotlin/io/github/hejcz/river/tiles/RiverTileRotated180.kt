package io.github.hejcz.river.tiles

import io.github.hejcz.basic.tiles.TileRotated180

class RiverTileRotated180(private val tile: RiverTile) : TileRotated180(tile),
    RiverTile {
    override fun exploreRiver(): Collection<io.github.hejcz.core.Direction> = tile.exploreRiver().map { it.right().right() }
}
