package io.github.hejcz.river.tiles

import io.github.hejcz.basic.tiles.GreenFieldExplorable
import io.github.hejcz.basic.tiles.RegionGreenFieldExplorable
import io.github.hejcz.core.*

object TileBB6F10 : RiverTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Up), Location(Right, LeftSide), Location(Left), Location(Down, RightSide)),
    setOf(Location(Right, RightSide), Location(Down, LeftSide))
) {
    override fun exploreRiver(): Collection<io.github.hejcz.core.Direction> = setOf(Down, Right)
    override fun exploreCastle(direction: Direction): Collection<io.github.hejcz.core.Direction> = emptySet()
    override fun exploreRoad(direction: Direction): Collection<io.github.hejcz.core.Direction> = emptySet()
    override fun hasGarden(): Boolean = true
}
