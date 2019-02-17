package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.Direction
import io.github.hejcz.placement.Location

object NoTile : Tile {
    override fun exploreCastle(direction: Direction) = emptySet<Direction>()
    override fun exploreGreenFields(location: Location) = emptySet<Location>()
    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}