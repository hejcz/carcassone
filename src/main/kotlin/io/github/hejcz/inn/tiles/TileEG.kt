package io.github.hejcz.inn.tiles

import io.github.hejcz.basic.tile.*
import io.github.hejcz.core.*

object TileEG : InnTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Up)),
    setOf(Location(Down), Location(Right))
) {
    override fun exploreCastle(direction: Direction): Directions = setOf(Left)
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}
