package io.github.hejcz.tiles.river

import io.github.hejcz.placement.*
import io.github.hejcz.tiles.basic.GreenFieldExplorable
import io.github.hejcz.tiles.basic.RegionGreenFieldExplorable

object TileBB6F8 : RiverTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Up), Location(Left, RightSide), Location(Right, LeftSide)),
    setOf(Location(Left, LeftSide)),
    setOf(Location(Right, RightSide))
) {
    override fun exploreRiver(): Collection<Direction> = setOf(Left, Right)
    override fun exploreCastle(direction: Direction): Collection<Direction> = emptySet()
    override fun exploreRoad(direction: Direction): Collection<Direction> = setOf(Down)
    override fun hasCloister(): Boolean = true
}