package io.github.hejcz.inn.tiles

import io.github.hejcz.basic.tile.*
import io.github.hejcz.core.*

object TileEO : InnTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Down))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.sameIfOneOf(Up, Left, Right)
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

