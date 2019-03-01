package io.github.hejcz.basic.tiles
import io.github.hejcz.core.*
object TileQ : Tile {
    override fun hasEmblem(): Boolean = true

    override fun exploreGreenFields(location: Location): Locations = setOf(
        Location(Down)
    )

    override fun exploreCastle(direction: Direction): Directions = setOf(
        Up,
        Left,
        Right
    )

    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}
