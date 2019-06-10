package io.github.hejcz.river.tiles

import io.github.hejcz.basic.tiles.*
import io.github.hejcz.core.*

object TileBB6F4 : RiverTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Left, RightSide), Location(Right, LeftSide), Location(Up)),
    setOf(Location(Left, LeftSide), Location(Right, RightSide), Location(Down))
) {
    override fun exploreRiver(): Directions = setOf(Left, Right)
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}
