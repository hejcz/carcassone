package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.*

object TileD : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(
        Location(Left, RightSide),
        Location(Right, LeftSide)
    ),
    setOf(
        Location(Left, LeftSide),
        Location(Right, RightSide),
        Location(Down)
    )
) {
    override fun exploreCastle(direction: Direction): Collection<Direction> = setOf(Up)
    override fun exploreRoad(direction: Direction): Collection<Direction> = setOf(
        Left,
        Right
    )
}