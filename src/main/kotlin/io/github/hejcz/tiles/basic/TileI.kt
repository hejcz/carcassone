package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.*

object TileI : Tile {
    override fun exploreGreenFields(location: Location): Collection<Location> = setOf(
            Location(Left),
            Location(Down)
    )

    override fun exploreCastle(direction: Direction): Collection<Direction> = when (direction) {
        Up, Left -> setOf(direction)
        else -> emptySet()
    }
    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}