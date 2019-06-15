package io.github.hejcz.basic.tile

import io.github.hejcz.core.*

object NoTile : Tile {
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreGreenFields(location: Location): Locations = emptySet()
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}
