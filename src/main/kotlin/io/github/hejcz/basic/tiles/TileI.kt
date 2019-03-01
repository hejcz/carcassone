package io.github.hejcz.basic.tiles
import io.github.hejcz.core.*
object TileI : Tile {
    override fun exploreGreenFields(location: Location): Locations = setOf(
        Location(Left),
        Location(Down)
    )

    override fun exploreCastle(direction: Direction): Directions = when (direction) {
        Up, Left -> setOf(direction)
        else -> emptySet()
    }

    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}
