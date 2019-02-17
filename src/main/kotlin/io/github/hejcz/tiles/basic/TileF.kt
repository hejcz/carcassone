package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.*

object TileF : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Up)),
    setOf(Location(Down))
) {
    override fun exploreCastle(direction: Direction): Collection<Direction> = setOf(
            Left,
            Right
    )

    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
    override fun hasEmblem(): Boolean = true
}