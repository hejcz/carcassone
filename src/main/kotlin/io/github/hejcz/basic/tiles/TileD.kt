package io.github.hejcz.basic.tiles

import io.github.hejcz.core.*
import io.github.hejcz.core.*
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
    override fun exploreCastle(direction: Direction): Collection<io.github.hejcz.core.Direction> = setOf(Up)
    override fun exploreRoad(direction: Direction): Collection<io.github.hejcz.core.Direction> = setOf(
        Left,
        Right
    )
}
