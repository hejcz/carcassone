package io.github.hejcz.inn.tiles

import io.github.hejcz.basic.tile.*
import io.github.hejcz.core.*

object TileEE : InnTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Up, LeftSide), Location(Left, RightSide)),
    setOf(Location(Down, LeftSide), Location(Right, RightSide)),
    setOf(Location(Up, RightSide), Location(Right, LeftSide), Location(Down, RightSide), Location(Left, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}
