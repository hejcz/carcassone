package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.Direction
import io.github.hejcz.placement.Location

open class TileRotated180(private val tile: Tile) : Tile {
    override fun exploreGreenFields(location: Location): Collection<Location> =
        tile.exploreGreenFields(location.left().left()).map { it.right().right() }

    override fun hasCloister(): Boolean = tile.hasCloister()
    override fun hasGarden(): Boolean = tile.hasGarden()
    override fun exploreCastle(direction: Direction): Collection<Direction> =
        tile.exploreCastle(direction.left().left()).map { it.right().right() }

    override fun exploreRoad(direction: Direction): Collection<Direction> =
        tile.exploreRoad(direction.left().left()).map { it.right().right() }

    override fun hasEmblem(): Boolean = tile.hasEmblem()
}