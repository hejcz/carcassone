package io.github.hejcz.basic.tiles

import io.github.hejcz.core.*
import io.github.hejcz.core.*
object TileU : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(
        Location(Left),
        Location(Up, LeftSide),
        Location(Down, RightSide)
    ),
    setOf(
        Location(Right),
        Location(Up, RightSide),
        Location(Down, LeftSide)
    )
) {
    override fun exploreCastle(direction: Direction) = emptySet<Direction>()
    override fun exploreRoad(direction: Direction) = setOf(Down, Up)
}
