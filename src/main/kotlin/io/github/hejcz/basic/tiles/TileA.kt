package io.github.hejcz.basic.tiles

import io.github.hejcz.core.*
import io.github.hejcz.core.*
object TileA : Tile {
    override fun hasCloister(): Boolean = true
    override fun exploreCastle(direction: Direction) = emptySet<io.github.hejcz.core.Direction>()
    override fun exploreGreenFields(location: Location): Collection<io.github.hejcz.core.Location> =
        setOf(
            Location(Right),
            Location(Up),
            Location(Left),
            Location(Down, RightSide),
            Location(Down, LeftSide)
        )

    override fun exploreRoad(direction: Direction): Collection<io.github.hejcz.core.Direction> = setOf(Down)
}
