package io.github.hejcz.basic.tiles
import io.github.hejcz.core.*
object TileE : Tile {
    override fun exploreGreenFields(location: Location): Locations = setOf(
        Location(Left),
        Location(Down),
        Location(Right)
    )

    override fun exploreCastle(direction: Direction): Directions = setOf(Up)
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}
