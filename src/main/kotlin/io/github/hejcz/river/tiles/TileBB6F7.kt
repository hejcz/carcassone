package io.github.hejcz.river.tiles

import io.github.hejcz.basic.tiles.*
import io.github.hejcz.core.*

object TileBB6F7 : RiverTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Up, LeftSide), Location(Left, RightSide)),
    setOf(Location(Up, RightSide), Location(Down), Location(Left, LeftSide), Location(Right))
) {
    override fun exploreRiver(): Directions = setOf(Up, Left)
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}
