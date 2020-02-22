package io.github.hejcz.base.tile

import io.github.hejcz.api.Direction
import io.github.hejcz.api.Directions
import io.github.hejcz.api.Location
import io.github.hejcz.api.Locations

object NoTile : BasicTile {
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreGreenFields(location: Location): Locations = emptySet()
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}
