package io.github.hejcz.inn.tiles

import io.github.hejcz.basic.tiles.TileRotated180
import io.github.hejcz.basic.tiles.TileRotated90
import io.github.hejcz.core.Direction

class InnTileRotated180(private val tile: InnTile) : TileRotated180(tile), InnTile {
    override fun isInnOnRoad(direction: Direction): Boolean = tile.isInnOnRoad(direction.right().right())
}
