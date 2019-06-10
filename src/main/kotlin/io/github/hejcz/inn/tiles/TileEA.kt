package io.github.hejcz.inn.tiles

import io.github.hejcz.basic.tiles.*
import io.github.hejcz.core.*

object TileEA : InnTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Up), Location(Right), Location(Down, LeftSide), Location(Left, RightSide)),
    setOf(Location(Down, RightSide), Location(Left, LeftSide))
) {
    override fun isInnOnRoad(direction: Direction): Boolean = direction in setOf(Left, Down)
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = setOf(Left, Down)
}
