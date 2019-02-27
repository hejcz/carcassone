package io.github.hejcz.basic.tiles
import io.github.hejcz.core.*
object TileI : Tile {
    override fun exploreGreenFields(location: Location): Collection<io.github.hejcz.core.Location> = setOf(
        Location(Left),
        Location(Down)
    )

    override fun exploreCastle(direction: Direction): Collection<io.github.hejcz.core.Direction> = when (direction) {
        Up, Left -> setOf(direction)
        else -> emptySet()
    }

    override fun exploreRoad(direction: Direction) = emptySet<io.github.hejcz.core.Direction>()
}
