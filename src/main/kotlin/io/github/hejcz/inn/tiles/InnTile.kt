package io.github.hejcz.inn.tiles

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*

interface InnTile : Tile {
    fun isInnOnRoad(direction: Direction): Boolean = false
    fun hasCathedral() = false

    override fun rotate(rotation: Rotation): Tile = when (rotation) {
        NoRotation -> this
        Rotation90 -> InnTileRotated90(this)
        Rotation180 -> InnTileRotated180(this)
        Rotation270 -> InnTileRotated270(this)
    }
}
