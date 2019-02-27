package io.github.hejcz.tiles.river

import io.github.hejcz.placement.*
import io.github.hejcz.tiles.basic.GreenFieldExplorable
import io.github.hejcz.tiles.basic.RegionGreenFieldExplorable

object TileBB6F7 : RiverTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Up, LeftSide), Location(Left, RightSide)),
    setOf(Location(Up, RightSide), Location(Down), Location(Left, LeftSide), Location(Right))
) {
    override fun exploreRiver(): Collection<Direction> = setOf(Up, Left)
    override fun exploreCastle(direction: Direction): Collection<Direction> = emptySet()
    override fun exploreRoad(direction: Direction): Collection<Direction> = emptySet()
}