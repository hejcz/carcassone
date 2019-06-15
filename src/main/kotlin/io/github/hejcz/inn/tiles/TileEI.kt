package io.github.hejcz.inn.tiles

import io.github.hejcz.basic.tile.*
import io.github.hejcz.core.*

object TileEI : InnTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Left, RightSide)),
    setOf(Location(Left, LeftSide)),
    setOf(Location(Right, RightSide)),
    setOf(Location(Right, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.sameIfOneOf(Up, Down)

    override fun exploreRoad(direction: Direction): Directions = direction.sameIfOneOf(Left, Right)
}
