package io.github.hejcz.tiles.river

import io.github.hejcz.placement.*
import io.github.hejcz.tiles.basic.GreenFieldExplorable
import io.github.hejcz.tiles.basic.RegionGreenFieldExplorable

object TileBB6F2 : RiverTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Left, RightSide)),
    setOf(Location(Left, LeftSide), Location(Down, RightSide)),
    setOf(Location(Right, RightSide), Location(Down, LeftSide)),
    setOf(Location(Right, LeftSide))
) {
    override fun exploreRiver(): Collection<Direction> = setOf(Left, Right)
    override fun exploreCastle(direction: Direction): Collection<Direction> = setOf(Up)
    override fun exploreRoad(direction: Direction): Collection<Direction> = setOf(Down)
}
