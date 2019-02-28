package io.github.hejcz.basic.tiles

import io.github.hejcz.core.Down
import io.github.hejcz.core.LeftSide
import io.github.hejcz.core.Location
import io.github.hejcz.core.RightSide
import io.github.hejcz.core.*
object TileT : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Down, RightSide)),
    setOf(Location(Down, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Collection<io.github.hejcz.core.Direction> = setOf(
        Up,
        Left,
        Right
    )

    override fun exploreRoad(direction: Direction): Collection<io.github.hejcz.core.Direction> = setOf(Down)
}
