package io.github.hejcz.tiles.river

import io.github.hejcz.placement.*
import io.github.hejcz.tiles.basic.GreenFieldExplorable
import io.github.hejcz.tiles.basic.RegionGreenFieldExplorable

object TileBB6F5 : RiverTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Left, RightSide), Location(Down, LeftSide)),
    setOf(Location(Left, LeftSide), Location(Down, RightSide))
) {
    override fun exploreRiver(): Collection<Direction> = setOf(Left, Down)
    override fun exploreCastle(direction: Direction): Collection<Direction> = setOf(Up, Right)
    override fun exploreRoad(direction: Direction): Collection<Direction> = emptySet()
}
