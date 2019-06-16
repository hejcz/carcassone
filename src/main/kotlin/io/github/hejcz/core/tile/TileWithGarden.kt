package io.github.hejcz.core.tile

import io.github.hejcz.core.*

class TileWithGarden(tile: Tile) : Tile by tile {

    override fun hasGarden(): Boolean = true

    // rotation returns new instance of Tile not modifying it
    // so call to this method can't be proxied
    override fun rotate(rotation: Rotation): Tile = when (rotation) {
        NoRotation -> this
        Rotation90 -> TileRotated90(this)
        Rotation180 -> TileRotated180(this)
        Rotation270 -> TileRotated270(this)
    }
}
