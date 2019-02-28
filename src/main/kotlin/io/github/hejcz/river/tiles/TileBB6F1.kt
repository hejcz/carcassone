package io.github.hejcz.river.tiles

import io.github.hejcz.core.*

object TileBB6F1 : RiverTile {
    override fun exploreRiver(): Collection<io.github.hejcz.core.Direction> = setOf(Down)
    override fun exploreCastle(direction: Direction): Collection<io.github.hejcz.core.Direction> = emptySet()
    override fun exploreRoad(direction: Direction): Collection<io.github.hejcz.core.Direction> = emptySet()
    override fun exploreGreenFields(location: Location): Collection<io.github.hejcz.core.Location> =
        setOf(Location(Up), Location(Down, RightSide), Location(Down, LeftSide), Location(Left), Location(Right))
}
