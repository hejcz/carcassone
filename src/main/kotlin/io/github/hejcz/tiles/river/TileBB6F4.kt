package io.github.hejcz.tiles.river

import io.github.hejcz.placement.*
import io.github.hejcz.tiles.basic.GreenFieldExplorable
import io.github.hejcz.tiles.basic.RegionGreenFieldExplorable

object TileBB6F4 : RiverTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Left, RightSide), Location(Right, LeftSide), Location(Up)),
    setOf(Location(Left, LeftSide), Location(Right, RightSide), Location(Down))
) {
    override fun exploreRiver(): Collection<Direction> = setOf(Left, Right)
    override fun exploreCastle(direction: Direction): Collection<Direction> = emptySet()
    override fun exploreRoad(direction: Direction): Collection<Direction> = emptySet()
}
