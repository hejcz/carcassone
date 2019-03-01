package io.github.hejcz.basic.tiles

import io.github.hejcz.core.*
import io.github.hejcz.core.*
object TileX : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(
        Location(Left, RightSide),
        Location(Up, LeftSide)
    ),
    setOf(
        Location(Left, LeftSide),
        Location(Down, RightSide)
    ),
    setOf(
        Location(Right, RightSide),
        Location(Down, LeftSide)
    ),
    setOf(
        Location(Right, LeftSide),
        Location(Up, RightSide)
    )
) {
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = setOf(direction)
}
