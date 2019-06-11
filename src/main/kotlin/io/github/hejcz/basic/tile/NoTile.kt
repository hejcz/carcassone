package io.github.hejcz.basic.tile

import io.github.hejcz.core.*

object NoTile : Tile {
    override fun exploreCastle(direction: Direction) = emptySet<Direction>()
    override fun exploreGreenFields(location: Location) = emptySet<Location>()
    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}
