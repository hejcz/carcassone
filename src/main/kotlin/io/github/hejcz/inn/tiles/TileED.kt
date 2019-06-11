package io.github.hejcz.inn.tiles

import io.github.hejcz.basic.tile.*
import io.github.hejcz.core.*

object TileED : InnTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Up), Location(Right, LeftSide), Location(Left, RightSide)),
    setOf(Location(Down), Location(Right, RightSide), Location(Left, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = when (direction) {
        is Left, Down -> setOf(direction)
        else -> emptySet()
    }
    override fun hasCloister(): Boolean = true
}
