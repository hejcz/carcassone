package io.github.hejcz.inn.tiles

import io.github.hejcz.basic.tiles.*
import io.github.hejcz.core.*

object TileEF : InnTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Right, LeftSide)),
    setOf(Location(Down), Location(Right, RightSide))
) {
    override fun exploreCastle(direction: Direction): Directions = when (direction) {
        in setOf(Left, Up) -> setOf(Left, Up)
        else -> emptySet()
    }

    override fun exploreRoad(direction: Direction): Directions = when (direction) {
        Right -> setOf(Right)
        else -> emptySet()
    }
}
