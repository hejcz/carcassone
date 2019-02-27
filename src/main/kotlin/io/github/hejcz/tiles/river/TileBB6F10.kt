package io.github.hejcz.tiles.river

import io.github.hejcz.placement.*
import io.github.hejcz.tiles.basic.GreenFieldExplorable
import io.github.hejcz.tiles.basic.RegionGreenFieldExplorable

object TileBB6F10 : RiverTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Up), Location(Right, LeftSide), Location(Left), Location(Down, RightSide)),
    setOf(Location(Right, RightSide), Location(Down, LeftSide))
) {
    override fun exploreRiver(): Collection<Direction> = setOf(Down, Right)
    override fun exploreCastle(direction: Direction): Collection<Direction> = emptySet()
    override fun exploreRoad(direction: Direction): Collection<Direction> = emptySet()
}