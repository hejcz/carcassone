package io.github.hejcz.inn.tiles

import io.github.hejcz.basic.tile.*
import io.github.hejcz.core.*

object TileEJ : InnTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Left), Location(Down, RightSide)),
    setOf(Location(Right), Location(Down, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.sameIfOneOf(Up)
    override fun exploreRoad(direction: Direction): Directions = direction.sameIfOneOf(Left, Right)
}

