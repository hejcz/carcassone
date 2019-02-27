package io.github.hejcz.basic.tiles
import io.github.hejcz.core.*
object TileN : Tile {
    override fun exploreGreenFields(location: Location): Collection<io.github.hejcz.core.Location> = setOf(
        Location(Left),
        Location(Down)
    )

    override fun exploreCastle(direction: Direction): Collection<io.github.hejcz.core.Direction> = setOf(
        Up,
        Right
    )

    override fun exploreRoad(direction: Direction) = emptySet<io.github.hejcz.core.Direction>()
}
