package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.*

object TileK : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(
        Location(Left, RightSide),
        Location(Right),
        Location(Down, LeftSide)
    ),
    setOf(
        Location(Left, LeftSide),
        Location(Down, RightSide)
    )
) {
    override fun exploreCastle(direction: Direction): Collection<Direction> = setOf(Up)
    override fun exploreRoad(direction: Direction): Collection<Direction> = setOf(
        Down,
        Left
    )
}