package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.Direction
import io.github.hejcz.placement.Location

open class TileRotated90(private val tile: Tile) : Tile {
    override fun exploreGreenFields(location: Location): Collection<Location> =
        tile.exploreGreenFields(location.left()).map { it.right() }

    override fun hasCloister(): Boolean = tile.hasCloister()
    override fun hasGarden(): Boolean = tile.hasGarden()
    override fun exploreCastle(direction: Direction): Collection<Direction> =
        tile.exploreCastle(direction.left()).map { it.right() }

    override fun exploreRoad(direction: Direction): Collection<Direction> =
        tile.exploreRoad(direction.left()).map { it.right() }

    override fun hasEmblem(): Boolean = tile.hasEmblem()
}