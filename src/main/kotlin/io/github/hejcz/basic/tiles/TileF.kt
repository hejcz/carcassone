package io.github.hejcz.basic.tiles

import io.github.hejcz.core.Down
import io.github.hejcz.core.Location
import io.github.hejcz.core.Up
import io.github.hejcz.core.*
object TileF : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Up)),
    setOf(Location(Down))
) {
    override fun exploreCastle(direction: Direction): Collection<io.github.hejcz.core.Direction> = setOf(
        Left,
        Right
    )

    override fun exploreRoad(direction: Direction) = emptySet<io.github.hejcz.core.Direction>()
    override fun hasEmblem(): Boolean = true
}
