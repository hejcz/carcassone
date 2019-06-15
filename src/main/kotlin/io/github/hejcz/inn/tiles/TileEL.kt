package io.github.hejcz.inn.tiles

import io.github.hejcz.basic.tile.*
import io.github.hejcz.core.*

object TileEL : InnTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Right, RightSide), Location(Down, LeftSide)),
    setOf(Location(Right, LeftSide), Location(Down, RightSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(Up, Left )
    override fun exploreRoad(direction: Direction): Directions = direction.allIfOneOf(Right, Down)
    override fun hasEmblem(direction: Direction): Boolean = true
    override fun isInnOnRoad(direction: Direction): Boolean = direction == Right || direction == Down
}

