package io.github.hejcz.inn.tiles

import io.github.hejcz.basic.tile.*
import io.github.hejcz.core.*

class InnTileRotated90(private val tile: InnTile) : TileRotated90(tile), InnTile {
    override fun isInnOnRoad(direction: Direction): Boolean = tile.isInnOnRoad(direction.right())
}
