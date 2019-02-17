package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.*

object TileG : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Up)),
    setOf(Location(Down))
) {
    override fun exploreCastle(direction: Direction): Collection<Direction> = setOf(
            Left,
            Right
    )

    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}