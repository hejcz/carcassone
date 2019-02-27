package io.github.hejcz.river.tiles

import io.github.hejcz.basic.tiles.TileRotated270

class RiverTileRotated270(private val tile: RiverTile) : TileRotated270(tile),
    RiverTile {
    override fun exploreRiver(): Collection<io.github.hejcz.core.Direction> = tile.exploreRiver().map { it.left() }
}
