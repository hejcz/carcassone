package io.github.hejcz.basic.tiles

import io.github.hejcz.core.*
import io.github.hejcz.core.*
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
    override fun exploreCastle(direction: Direction): Collection<io.github.hejcz.core.Direction> = setOf(Up)
    override fun exploreRoad(direction: Direction): Collection<io.github.hejcz.core.Direction> = setOf(
        Down,
        Right
    )
}
