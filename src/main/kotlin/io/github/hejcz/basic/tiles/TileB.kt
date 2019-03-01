package io.github.hejcz.basic.tiles
import io.github.hejcz.core.*
object TileB : Tile {
    override fun hasCloister(): Boolean = true
    override fun exploreCastle(direction: Direction) = emptySet<Direction>()
    override fun exploreGreenFields(location: Location): Locations =
        setOf(
            Location(Right),
            Location(Up),
            Location(Left),
            Location(Down)
        )

    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}
