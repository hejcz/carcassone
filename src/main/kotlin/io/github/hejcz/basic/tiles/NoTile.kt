package io.github.hejcz.basic.tiles

import io.github.hejcz.core.Direction
import io.github.hejcz.core.Location

object NoTile : Tile {
    override fun exploreCastle(direction: Direction) = emptySet<io.github.hejcz.core.Direction>()
    override fun exploreGreenFields(location: Location) = emptySet<io.github.hejcz.core.Location>()
    override fun exploreRoad(direction: Direction) = emptySet<io.github.hejcz.core.Direction>()
}
