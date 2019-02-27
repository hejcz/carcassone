package io.github.hejcz.river.tiles

import io.github.hejcz.basic.tiles.GreenFieldExplorable
import io.github.hejcz.basic.tiles.RegionGreenFieldExplorable
import io.github.hejcz.core.*

object TileBB6F8 : RiverTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Up), Location(Left, RightSide), Location(Right, LeftSide)),
    setOf(Location(Left, LeftSide)),
    setOf(Location(Right, RightSide))
) {
    override fun exploreRiver(): Collection<io.github.hejcz.core.Direction> = setOf(Left, Right)
    override fun exploreCastle(direction: Direction): Collection<io.github.hejcz.core.Direction> = emptySet()
    override fun exploreRoad(direction: Direction): Collection<io.github.hejcz.core.Direction> = setOf(Down)
    override fun hasCloister(): Boolean = true
}
