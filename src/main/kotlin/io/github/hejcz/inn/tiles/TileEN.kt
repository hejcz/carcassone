package io.github.hejcz.inn.tiles

import io.github.hejcz.basic.tile.*
import io.github.hejcz.core.*

object TileEN : InnTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Right), Location(Down, LeftSide)),
    setOf(Location(Down, RightSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(Up, Left)
    override fun exploreRoad(direction: Direction): Directions = direction.sameIfOneOf(Down)
    override fun isInnOnRoad(direction: Direction): Boolean = direction == Down
}

