package io.github.hejcz.river.tiles

import io.github.hejcz.basic.tile.*
import io.github.hejcz.core.*

object TileBB6F3 : RiverTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Left, RightSide), Location(Right, LeftSide)),
    setOf(Location(Left, LeftSide), Location(Right, RightSide))
) {
    override fun exploreRiver(): Directions = setOf(Left, Right)
    override fun exploreCastle(direction: Direction): Directions = direction.sameIfOneOf(Up, Down)

    override fun exploreRoad(direction: Direction): Directions = emptySet()
}
