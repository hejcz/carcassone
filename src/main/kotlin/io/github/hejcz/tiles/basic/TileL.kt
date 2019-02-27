package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.*

object TileL : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(
        Location(Left, RightSide),
        Location(Right, LeftSide)
    ),
    setOf(
        Location(Left, LeftSide),
        Location(Down, RightSide)
    ),
    setOf(
        Location(Right, RightSide),
        Location(Down, LeftSide)
    )
) {
    override fun exploreCastle(direction: Direction): Collection<Direction> = setOf(Up)
    override fun exploreRoad(direction: Direction): Collection<Direction> = when (direction) {
        Left, Right, Down -> setOf(direction)
        else -> emptySet()
    }
}