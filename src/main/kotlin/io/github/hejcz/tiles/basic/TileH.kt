package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.*

object TileH : Tile {
    override fun exploreGreenFields(location: Location): Collection<Location> = setOf(
            Location(Up),
            Location(Down)
    )

    override fun exploreCastle(direction: Direction): Collection<Direction> = when (direction) {
        Left, Right -> setOf(direction)
        else -> emptySet()
    }
    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}