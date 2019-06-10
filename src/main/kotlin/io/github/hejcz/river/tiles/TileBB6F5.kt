package io.github.hejcz.river.tiles

import io.github.hejcz.basic.tiles.*
import io.github.hejcz.core.*

object TileBB6F5 : RiverTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Left, RightSide), Location(Down, LeftSide)),
    setOf(Location(Left, LeftSide), Location(Down, RightSide))
) {
    override fun exploreRiver(): Directions = setOf(Left, Down)
    override fun exploreCastle(direction: Direction): Directions = setOf(Up, Right)
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}
