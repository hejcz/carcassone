package io.github.hejcz.inn.tiles

import io.github.hejcz.basic.tile.*
import io.github.hejcz.core.*

object TileEF : InnTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Right, LeftSide)),
    setOf(Location(Down), Location(Right, RightSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(Left, Up)
    override fun exploreRoad(direction: Direction): Directions = direction.sameIfOneOf(Right)
}
