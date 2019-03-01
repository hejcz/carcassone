package io.github.hejcz.inn.tiles

import io.github.hejcz.core.*

object TileEH : InnTile {
    override fun exploreGreenFields(location: Location): Locations = setOf(Location(Center))
    override fun exploreCastle(direction: Direction): Directions = setOf(direction)
    override fun exploreRoad(direction: Direction): Directions = emptySet()
    override fun hasGarden(): Boolean = true
}
