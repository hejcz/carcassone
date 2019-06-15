package io.github.hejcz.inn.tiles

import io.github.hejcz.basic.tile.*
import io.github.hejcz.core.*

object TileEQ : InnTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Up, LeftSide)),
    setOf(Location(Down, LeftSide)),
    setOf(Location(Up, RightSide)),
    setOf(Location(Down, RightSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(Left, Right)
    override fun exploreRoad(direction: Direction): Directions = direction.sameIfOneOf(Up, Down)
    override fun hasEmblem(direction: Direction): Boolean = true
}

