package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.*

object TileQ : Tile {
    override fun hasEmblem(): Boolean = true

    override fun exploreGreenFields(location: Location): Collection<Location> = setOf(
            Location(Down)
    )

    override fun exploreCastle(direction: Direction): Collection<Direction> = setOf(
            Up,
            Left,
            Right
    )

    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}