package io.github.hejcz.inn.tiles

import io.github.hejcz.basic.tile.*
import io.github.hejcz.core.*

object TileEC : InnTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Up), Location(Right, LeftSide), Location(Left, RightSide)),
    setOf(Location(Down, RightSide), Location(Left, LeftSide)),
    setOf(Location(Down, LeftSide), Location(Right, RightSide))
) {
    override fun isInnOnRoad(direction: Direction): Boolean = direction in setOf(Left, Right)
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = when (direction) {
        is Left, Down, Right -> setOf(direction)
        else -> emptySet()
    }
}
