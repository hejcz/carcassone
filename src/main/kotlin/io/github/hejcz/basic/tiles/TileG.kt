package io.github.hejcz.basic.tiles

import io.github.hejcz.core.Down
import io.github.hejcz.core.Location
import io.github.hejcz.core.Up
import io.github.hejcz.core.*
object TileG : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Up)),
    setOf(Location(Down))
) {
    override fun exploreCastle(direction: Direction): Directions = setOf(
        Left,
        Right
    )

    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}
