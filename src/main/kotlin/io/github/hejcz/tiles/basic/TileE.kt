package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.*

object TileE : Tile {
    override fun exploreGreenFields(location: Location): Collection<Location> = setOf(
        Location(Left),
        Location(Down),
        Location(Right)
    )

    override fun exploreCastle(direction: Direction): Collection<Direction> = setOf(Up)
    override fun exploreRoad(direction: Direction): Collection<Direction> = emptySet()
}