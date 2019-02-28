package io.github.hejcz.basic.tiles

import io.github.hejcz.core.*
import io.github.hejcz.core.*
object TileW : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(
        Location(Left, RightSide),
        Location(Right, LeftSide),
        Location(Up)
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
    override fun exploreCastle(direction: Direction): Collection<io.github.hejcz.core.Direction> = emptySet()
    override fun exploreRoad(direction: Direction): Collection<io.github.hejcz.core.Direction> = when (direction) {
        Left, Right, Down -> setOf(direction)
        else -> emptySet()
    }
}
