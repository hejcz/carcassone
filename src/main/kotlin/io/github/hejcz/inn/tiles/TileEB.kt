package io.github.hejcz.inn.tiles

import io.github.hejcz.basic.tiles.*
import io.github.hejcz.core.*

object TileEB : InnTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Up), Location(Right, LeftSide), Location(Left, RightSide)),
    setOf(Location(Down), Location(Right, RightSide), Location(Left, LeftSide))
) {
    override fun isInnOnRoad(direction: Direction): Boolean = direction in setOf(Left, Right)
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = setOf(Left, Right)
    override fun hasGarden(): Boolean = true
}
