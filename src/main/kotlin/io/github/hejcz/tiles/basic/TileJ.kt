package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.*

object TileJ : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(
            Location(Left),
            Location(Right, LeftSide),
            Location(Down, RightSide)
    ),
    setOf(
            Location(Right, RightSide),
            Location(Down, LeftSide)
    )
) {
    override fun exploreCastle(direction: Direction): Collection<Direction> = setOf(Up)
    override fun exploreRoad(direction: Direction): Collection<Direction> = setOf(
            Down,
            Right
    )
}