package io.github.hejcz.river.tiles

import io.github.hejcz.basic.tiles.TileRotated90

class RiverTileRotated90(private val tile: RiverTile) : TileRotated90(tile),
    RiverTile {
    override fun exploreRiver(): Collection<io.github.hejcz.core.Direction> = tile.exploreRiver().map { it.right() }
}
