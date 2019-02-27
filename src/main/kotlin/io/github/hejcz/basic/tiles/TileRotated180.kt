package io.github.hejcz.basic.tiles

import io.github.hejcz.core.Direction
import io.github.hejcz.core.Location

open class TileRotated180(private val tile: Tile) : Tile {
    override fun exploreGreenFields(location: Location): Collection<io.github.hejcz.core.Location> =
        tile.exploreGreenFields(location.left().left()).map { it.right().right() }

    override fun hasCloister(): Boolean = tile.hasCloister()
    override fun hasGarden(): Boolean = tile.hasGarden()
    override fun exploreCastle(direction: Direction): Collection<io.github.hejcz.core.Direction> =
        tile.exploreCastle(direction.left().left()).map { it.right().right() }

    override fun exploreRoad(direction: Direction): Collection<io.github.hejcz.core.Direction> =
        tile.exploreRoad(direction.left().left()).map { it.right().right() }

    override fun hasEmblem(): Boolean = tile.hasEmblem()
}
