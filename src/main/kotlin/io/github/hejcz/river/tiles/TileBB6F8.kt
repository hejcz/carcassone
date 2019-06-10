package io.github.hejcz.river.tiles

import io.github.hejcz.basic.tiles.*
import io.github.hejcz.core.*

object TileBB6F8 : RiverTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Up), Location(Left, RightSide), Location(Right, LeftSide)),
    setOf(Location(Left, LeftSide)),
    setOf(Location(Right, RightSide))
) {
    override fun exploreRiver(): Directions = setOf(Left, Right)
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = setOf(Down)
    override fun hasCloister(): Boolean = true
}
