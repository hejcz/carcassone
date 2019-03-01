package io.github.hejcz.river.tiles

import io.github.hejcz.core.*

object TileBB6F12 : RiverTile {
    override fun exploreRiver(): Directions = setOf(Down)
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = emptySet()
    override fun exploreGreenFields(location: Location): Locations =
        setOf(Location(Up), Location(Down, RightSide), Location(Down, LeftSide), Location(Left), Location(Right))
}
