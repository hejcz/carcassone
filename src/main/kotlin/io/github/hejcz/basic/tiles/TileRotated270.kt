package io.github.hejcz.basic.tiles

import io.github.hejcz.core.Direction
import io.github.hejcz.core.Location

open class TileRotated270(private val tile: Tile) : Tile {
    override fun exploreGreenFields(location: Location): Collection<io.github.hejcz.core.Location> =
        tile.exploreGreenFields(location.right()).map { it.left() }

    override fun hasCloister(): Boolean = tile.hasCloister()
    override fun hasGarden(): Boolean = tile.hasGarden()
    override fun exploreCastle(direction: Direction): Collection<io.github.hejcz.core.Direction> =
        tile.exploreCastle(direction.right()).map { it.left() }

    override fun exploreRoad(direction: Direction): Collection<io.github.hejcz.core.Direction> =
        tile.exploreRoad(direction.right()).map { it.left() }

    override fun hasEmblem(): Boolean = tile.hasEmblem()
}
