package io.github.hejcz.river.tiles

import io.github.hejcz.basic.tiles.GreenFieldExplorable
import io.github.hejcz.basic.tiles.RegionGreenFieldExplorable
import io.github.hejcz.core.*

object TileBB6F9 : RiverTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Up, LeftSide), Location(Left, RightSide)),
    setOf(Location(Up, RightSide), Location(Right, LeftSide), Location(Left, LeftSide), Location(Down, RightSide)),
    setOf(Location(Right, RightSide), Location(Down, LeftSide))
) {
    override fun exploreRiver(): Collection<io.github.hejcz.core.Direction> = setOf(Down, Right)
    override fun exploreCastle(direction: Direction): Collection<io.github.hejcz.core.Direction> = emptySet()
    override fun exploreRoad(direction: Direction): Collection<io.github.hejcz.core.Direction> = setOf(Up, Left)
}
