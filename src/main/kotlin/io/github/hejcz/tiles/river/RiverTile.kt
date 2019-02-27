package io.github.hejcz.tiles.river

import io.github.hejcz.placement.*
import io.github.hejcz.tiles.basic.Tile

interface RiverTile : Tile {
    fun exploreRiver(): Collection<Direction>
    override fun isValidNeighborFor(neighbor: Tile, direction: Direction): Boolean =
        super.isValidNeighborFor(neighbor, direction)
            || (neighbor is RiverTile && direction in exploreRiver() && direction.opposite() in neighbor.exploreRiver())

    override fun rotate(rotation: Rotation): Tile = when (rotation) {
        NoRotation -> this
        Rotation90 -> RiverTileRotated90(this)
        Rotation180 -> RiverTileRotated180(this)
        Rotation270 -> RiverTileRotated270(this)
    }
}
