package io.github.hejcz.expansion.inn.tiles

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*

class InnTileRotated270(private val tile: InnTile) : TileRotated270(tile), InnTile {
    override fun isInnOnRoad(direction: Direction): Boolean = tile.isInnOnRoad(direction.left())
}
