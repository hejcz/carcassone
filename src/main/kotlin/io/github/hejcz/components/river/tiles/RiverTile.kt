package io.github.hejcz.components.river.tiles

import io.github.hejcz.api.*
import io.github.hejcz.base.tile.*

interface RiverTile : BasicTile {
    fun exploreRiver(): Directions
    override fun isValidNeighborFor(other: Tile, direction: Direction): Boolean =
        super.isValidNeighborFor(other, direction) ||
            (other is RiverTile && direction in exploreRiver() && direction.opposite() in other.exploreRiver())

    override fun rotate(rotation: Rotation): Tile = when (rotation) {
        NoRotation -> this
        Rotation90 -> RiverTileRotated90(this)
        Rotation180 -> RiverTileRotated180(this)
        Rotation270 -> RiverTileRotated270(this)
    }
}
