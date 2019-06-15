package io.github.hejcz.inn.tiles

import io.github.hejcz.basic.tile.*
import io.github.hejcz.core.*

object TileEP : InnTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Down))
) {
    override fun exploreCastle(direction: Direction): Directions = when (direction) {
        Up, Right -> setOf(Up, Right)
        Left -> setOf(Left)
        else -> emptySet()
    }
    override fun exploreRoad(direction: Direction): Directions = emptySet()
    override fun hasEmblem(direction: Direction): Boolean = direction == Up || direction == Right
}

