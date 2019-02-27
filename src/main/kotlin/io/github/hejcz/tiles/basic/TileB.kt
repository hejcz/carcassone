package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.*

object TileB : Tile {
    override fun hasCloister(): Boolean = true
    override fun exploreCastle(direction: Direction) = emptySet<Direction>()
    override fun exploreGreenFields(location: Location): Collection<Location> =
        setOf(
            Location(Right),
            Location(Up),
            Location(Left),
            Location(Down)
        )

    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}