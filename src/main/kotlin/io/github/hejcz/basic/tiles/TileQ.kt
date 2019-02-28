package io.github.hejcz.basic.tiles
import io.github.hejcz.core.*
object TileQ : Tile {
    override fun hasEmblem(): Boolean = true

    override fun exploreGreenFields(location: Location): Collection<io.github.hejcz.core.Location> = setOf(
        Location(Down)
    )

    override fun exploreCastle(direction: Direction): Collection<io.github.hejcz.core.Direction> = setOf(
        Up,
        Left,
        Right
    )

    override fun exploreRoad(direction: Direction) = emptySet<io.github.hejcz.core.Direction>()
}
