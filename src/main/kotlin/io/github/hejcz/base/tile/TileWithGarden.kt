package io.github.hejcz.base.tile

import io.github.hejcz.api.*

class TileWithGarden(tile: BasicTile) : BasicTile by tile {

    override fun hasGarden(): Boolean = true

    // rotation returns new instance of Tile not modifying it
    // so call to this method can't be proxied
    override fun rotate(rotation: Rotation): BasicTile = when (rotation) {
        NoRotation -> this
        Rotation90 -> TileRotated90(this)
        Rotation180 -> TileRotated180(this)
        Rotation270 -> TileRotated270(this)
    }
}
