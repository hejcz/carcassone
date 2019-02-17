package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.*

object TileO : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Down, RightSide), Location(Right, LeftSide)),
    setOf(Location(Down, LeftSide), Location(Right, RightSide))
) {
    override fun hasEmblem(): Boolean = true
    override fun exploreCastle(direction: Direction): Collection<Direction> = setOf(Up, Left)
    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}