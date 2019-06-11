package io.github.hejcz.river.tiles

import io.github.hejcz.basic.tile.*
import io.github.hejcz.core.*

object TileBB6F2 : RiverTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Left, RightSide)),
    setOf(Location(Left, LeftSide), Location(Down, RightSide)),
    setOf(Location(Right, RightSide), Location(Down, LeftSide)),
    setOf(Location(Right, LeftSide))
) {
    override fun exploreRiver(): Directions = setOf(Left, Right)
    override fun exploreCastle(direction: Direction): Directions = setOf(Up)
    override fun exploreRoad(direction: Direction): Directions = setOf(Down)
}
