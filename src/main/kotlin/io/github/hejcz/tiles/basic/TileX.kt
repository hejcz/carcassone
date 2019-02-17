package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.*

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
    override fun exploreCastle(direction: Direction): Collection<Direction> = emptySet()
    override fun exploreRoad(direction: Direction): Collection<Direction> = setOf(direction)
}