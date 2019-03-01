package io.github.hejcz.basic.tiles

import io.github.hejcz.core.*
import io.github.hejcz.core.*
object TileA : Tile {
    override fun hasCloister(): Boolean = true
    override fun exploreCastle(direction: Direction) = emptySet<Direction>()
    override fun exploreGreenFields(location: Location): Locations =
        setOf(
            Location(Right),
            Location(Up),
            Location(Left),
            Location(Down, RightSide),
            Location(Down, LeftSide)
        )

    override fun exploreRoad(direction: Direction): Directions = setOf(Down)
}
