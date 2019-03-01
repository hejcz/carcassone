package io.github.hejcz.inn.tiles

import io.github.hejcz.core.*

object TileEK : InnTile {
    override fun exploreCastle(direction: Direction): Directions = setOf(Up, Down, Left, Right)
    override fun exploreGreenFields(location: Location): Locations = emptySet()
    override fun exploreRoad(direction: Direction): Directions = emptySet()
    override fun hasCathedral(): Boolean = true
}
