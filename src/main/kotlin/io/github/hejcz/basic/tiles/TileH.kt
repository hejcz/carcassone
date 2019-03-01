package io.github.hejcz.basic.tiles
import io.github.hejcz.core.*
object TileH : Tile {
    override fun exploreGreenFields(location: Location): Locations = setOf(
        Location(Up),
        Location(Down)
    )

    override fun exploreCastle(direction: Direction): Directions = when (direction) {
        Left, Right -> setOf(direction)
        else -> emptySet()
    }

    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}
