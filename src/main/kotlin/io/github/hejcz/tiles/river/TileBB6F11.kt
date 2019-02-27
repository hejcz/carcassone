package io.github.hejcz.tiles.river

import io.github.hejcz.placement.*
import io.github.hejcz.tiles.basic.GreenFieldExplorable
import io.github.hejcz.tiles.basic.RegionGreenFieldExplorable

object TileBB6F11 : RiverTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Left, RightSide), Location(Up, LeftSide)),
    setOf(Location(Left, LeftSide), Location(Down, RightSide)),
    setOf(Location(Right, RightSide), Location(Down, LeftSide)),
    setOf(Location(Right, LeftSide), Location(Up, RightSide))
) {
    override fun exploreRiver(): Collection<Direction> = setOf(Down, Up)
    override fun exploreCastle(direction: Direction): Collection<Direction> = emptySet()
    override fun exploreRoad(direction: Direction): Collection<Direction> = setOf(Left, Right)
}