package io.github.hejcz.basic.tiles

import io.github.hejcz.core.*
import io.github.hejcz.core.*
object TileP : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Down, RightSide), Location(Right, LeftSide)),
    setOf(Location(Down, LeftSide), Location(Right, RightSide))
) {
    override fun exploreCastle(direction: Direction): Collection<io.github.hejcz.core.Direction> = setOf(Up, Left)
    override fun exploreRoad(direction: Direction) = emptySet<io.github.hejcz.core.Direction>()
}
