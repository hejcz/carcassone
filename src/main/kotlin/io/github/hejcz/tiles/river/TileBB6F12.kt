package io.github.hejcz.tiles.river

import io.github.hejcz.placement.*

object TileBB6F12 : RiverTile {
    override fun exploreRiver(): Collection<Direction> = setOf(Down)
    override fun exploreCastle(direction: Direction): Collection<Direction> = emptySet()
    override fun exploreRoad(direction: Direction): Collection<Direction> = emptySet()
    override fun exploreGreenFields(location: Location): Collection<Location> =
        setOf(Location(Up), Location(Down, RightSide), Location(Down, LeftSide), Location(Left), Location(Right))
}
