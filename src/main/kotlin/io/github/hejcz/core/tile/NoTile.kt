package io.github.hejcz.core.tile

import io.github.hejcz.core.Direction
import io.github.hejcz.core.Directions
import io.github.hejcz.core.Location
import io.github.hejcz.core.Locations

object NoTile : Tile {
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreGreenFields(location: Location): Locations = emptySet()
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}
