package io.github.hejcz.inn.tiles

import io.github.hejcz.basic.tile.*
import io.github.hejcz.core.*

object TileEM : InnTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Left, RightSide), Location(Down, LeftSide), Location(Right)),
    setOf(Location(Left, LeftSide), Location(Down, RightSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.sameIfOneOf(Up)
    override fun exploreRoad(direction: Direction): Directions = direction.allIfOneOf(Down, Left)
    override fun isInnOnRoad(direction: Direction): Boolean = direction == Down || direction == Left
}

