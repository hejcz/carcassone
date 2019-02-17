package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.*

object TileA : Tile {
    override fun hasCloister(): Boolean = true
    override fun exploreCastle(direction: Direction) = emptySet<Direction>()
    override fun exploreGreenFields(location: Location): Collection<Location> =
        setOf(
                Location(Right),
                Location(Up),
                Location(Left),
                Location(Down, RightSide),
                Location(Down, LeftSide)
        )

    override fun exploreRoad(direction: Direction): Collection<Direction> = setOf(Down)
}