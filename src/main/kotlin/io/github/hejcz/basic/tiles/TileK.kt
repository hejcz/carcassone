package io.github.hejcz.basic.tiles

import io.github.hejcz.core.*
import io.github.hejcz.core.*
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
    override fun exploreCastle(direction: Direction): Directions = setOf(Up)
    override fun exploreRoad(direction: Direction): Directions = setOf(
        Down,
        Left
    )
}
