package io.github.hejcz.basic.tiles
import io.github.hejcz.core.*
object TileH : Tile {
    override fun exploreGreenFields(location: Location): Collection<io.github.hejcz.core.Location> = setOf(
        Location(Up),
        Location(Down)
    )

    override fun exploreCastle(direction: Direction): Collection<io.github.hejcz.core.Direction> = when (direction) {
        Left, Right -> setOf(direction)
        else -> emptySet()
    }

    override fun exploreRoad(direction: Direction) = emptySet<io.github.hejcz.core.Direction>()
}
