package io.github.hejcz.river.tiles

import io.github.hejcz.basic.tiles.GreenFieldExplorable
import io.github.hejcz.basic.tiles.RegionGreenFieldExplorable
import io.github.hejcz.core.*

object TileBB6F11 : RiverTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Left, RightSide), Location(Up, LeftSide)),
    setOf(Location(Left, LeftSide), Location(Down, RightSide)),
    setOf(Location(Right, RightSide), Location(Down, LeftSide)),
    setOf(Location(Right, LeftSide), Location(Up, RightSide))
) {
    override fun exploreRiver(): Collection<io.github.hejcz.core.Direction> = setOf(Down, Up)
    override fun exploreCastle(direction: Direction): Collection<io.github.hejcz.core.Direction> = emptySet()
    override fun exploreRoad(direction: Direction): Collection<io.github.hejcz.core.Direction> = setOf(Left, Right)
}
