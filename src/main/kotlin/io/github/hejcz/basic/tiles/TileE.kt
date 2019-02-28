package io.github.hejcz.basic.tiles
import io.github.hejcz.core.*
object TileE : Tile {
    override fun exploreGreenFields(location: Location): Collection<io.github.hejcz.core.Location> = setOf(
        Location(Left),
        Location(Down),
        Location(Right)
    )

    override fun exploreCastle(direction: Direction): Collection<io.github.hejcz.core.Direction> = setOf(Up)
    override fun exploreRoad(direction: Direction): Collection<io.github.hejcz.core.Direction> = emptySet()
}
