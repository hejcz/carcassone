package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.*

object TileN : Tile {
    override fun exploreGreenFields(location: Location): Collection<Location> = setOf(
            Location(Left),
            Location(Down)
    )

    override fun exploreCastle(direction: Direction): Collection<Direction> = setOf(
            Up,
            Right
    )

    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}