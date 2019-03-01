package io.github.hejcz.basic.tiles

import io.github.hejcz.core.Direction
import io.github.hejcz.core.Location

object NoTile : Tile {
    override fun exploreCastle(direction: Direction) = emptySet<Direction>()
    override fun exploreGreenFields(location: Location) = emptySet<Location>()
    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}
