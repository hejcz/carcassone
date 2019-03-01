package io.github.hejcz.basic.tiles
import io.github.hejcz.core.*
object TileC : Tile {
    override fun hasEmblem(): Boolean = true
    override fun exploreCastle(direction: Direction) = setOf(Up, Down, Left, Right)
    override fun exploreGreenFields(location: Location) = emptySet<Location>()
    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}
