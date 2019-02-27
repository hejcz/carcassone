package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.*

object TileS : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Down, RightSide)),
    setOf(Location(Down, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Collection<Direction> = setOf(
        Up,
        Left,
        Right
    )

    override fun exploreRoad(direction: Direction): Collection<Direction> = setOf(Down)
    override fun hasEmblem(): Boolean = true
}