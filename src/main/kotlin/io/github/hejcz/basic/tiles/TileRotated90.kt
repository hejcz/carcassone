package io.github.hejcz.basic.tiles

import io.github.hejcz.core.Direction
import io.github.hejcz.core.Location

open class TileRotated90(private val tile: Tile) : Tile {
    override fun exploreGreenFields(location: Location): Collection<io.github.hejcz.core.Location> =
        tile.exploreGreenFields(location.left()).map { it.right() }

    override fun hasCloister(): Boolean = tile.hasCloister()
    override fun hasGarden(): Boolean = tile.hasGarden()
    override fun exploreCastle(direction: Direction): Collection<io.github.hejcz.core.Direction> =
        tile.exploreCastle(direction.left()).map { it.right() }

    override fun exploreRoad(direction: Direction): Collection<io.github.hejcz.core.Direction> =
        tile.exploreRoad(direction.left()).map { it.right() }

    override fun hasEmblem(): Boolean = tile.hasEmblem()
}
