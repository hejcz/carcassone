package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.Direction
import io.github.hejcz.placement.Location

open class TileRotated270(private val tile: Tile) : Tile {
    override fun exploreGreenFields(location: Location): Collection<Location> =
        tile.exploreGreenFields(location.right()).map { it.left() }

    override fun hasCloister(): Boolean = tile.hasCloister()
    override fun hasGarden(): Boolean = tile.hasGarden()
    override fun exploreCastle(direction: Direction): Collection<Direction> =
        tile.exploreCastle(direction.right()).map { it.left() }

    override fun exploreRoad(direction: Direction): Collection<Direction> =
        tile.exploreRoad(direction.right()).map { it.left() }

    override fun hasEmblem(): Boolean = tile.hasEmblem()
}