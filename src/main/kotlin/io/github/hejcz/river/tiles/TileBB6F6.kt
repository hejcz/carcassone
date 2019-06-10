package io.github.hejcz.river.tiles

import io.github.hejcz.basic.tiles.*
import io.github.hejcz.core.*

object TileBB6F6 : RiverTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Up, LeftSide), Location(Down, RightSide), Location(Left)),
    setOf(Location(Up, RightSide), Location(Down, LeftSide), Location(Right))
) {
    override fun exploreRiver(): Directions = setOf(Up, Down)
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}
