package io.github.hejcz.inn.tiles

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*

class InnTileRotated180(private val tile: InnTile) : TileRotated180(tile), InnTile {
    override fun isInnOnRoad(direction: Direction): Boolean = tile.isInnOnRoad(direction.right().right())
}
