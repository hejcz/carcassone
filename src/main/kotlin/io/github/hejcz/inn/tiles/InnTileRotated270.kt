package io.github.hejcz.inn.tiles

import io.github.hejcz.basic.tile.*
import io.github.hejcz.core.*

class InnTileRotated270(private val tile: InnTile) : TileRotated270(tile), InnTile {
    override fun isInnOnRoad(direction: Direction): Boolean = tile.isInnOnRoad(direction.left())
}
