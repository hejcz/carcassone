package io.github.hejcz.tiles.river

import io.github.hejcz.placement.*
import io.github.hejcz.tiles.basic.GreenFieldExplorable
import io.github.hejcz.tiles.basic.RegionGreenFieldExplorable

object TileBB6F9 : RiverTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Up, LeftSide), Location(Left, RightSide)),
    setOf(Location(Up, RightSide), Location(Right, LeftSide), Location(Left, LeftSide), Location(Down, RightSide)),
    setOf(Location(Right, RightSide), Location(Down, LeftSide))
) {
    override fun exploreRiver(): Collection<Direction> = setOf(Down, Right)
    override fun exploreCastle(direction: Direction): Collection<Direction> = emptySet()
    override fun exploreRoad(direction: Direction): Collection<Direction> = setOf(Up, Left)
}