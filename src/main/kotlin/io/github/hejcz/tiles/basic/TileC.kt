package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.*

object TileC : Tile {
    override fun hasEmblem(): Boolean = true
    override fun exploreCastle(direction: Direction) = setOf(Up, Down, Left, Right)
    override fun exploreGreenFields(location: Location) = emptySet<Location>()
    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}