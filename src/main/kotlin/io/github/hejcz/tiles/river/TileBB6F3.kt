package io.github.hejcz.tiles.river

import io.github.hejcz.placement.*
import io.github.hejcz.tiles.basic.GreenFieldExplorable
import io.github.hejcz.tiles.basic.RegionGreenFieldExplorable

object TileBB6F3 : RiverTile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Left, RightSide), Location(Right, LeftSide)),
    setOf(Location(Left, LeftSide), Location(Right, RightSide))
) {
    override fun exploreRiver(): Collection<Direction> = setOf(Left, Right)
    override fun exploreCastle(direction: Direction): Collection<Direction> = when (direction) {
        Up, Down -> setOf(direction)
        else -> emptySet()
    }

    override fun exploreRoad(direction: Direction): Collection<Direction> = emptySet()
}
