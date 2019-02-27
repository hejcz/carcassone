package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.*

object TileV : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(
        Location(Left, LeftSide),
        Location(Down, RightSide)
    ),
    setOf(
        Location(Left, RightSide),
        Location(Down, LeftSide),
        Location(Up),
        Location(Right)
    )
) {
    override fun exploreCastle(direction: Direction) = emptySet<Direction>()
    override fun exploreRoad(direction: Direction) = setOf(Down, Left)
}