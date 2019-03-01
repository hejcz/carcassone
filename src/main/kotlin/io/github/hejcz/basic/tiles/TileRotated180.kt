package io.github.hejcz.basic.tiles

import io.github.hejcz.core.*

open class TileRotated180(private val tile: Tile) : Tile {
    override fun exploreGreenFields(location: Location): Locations =
        tile.exploreGreenFields(location.opposite()).map { it.opposite() }

    override fun hasCloister(): Boolean = tile.hasCloister()
    override fun hasGarden(): Boolean = tile.hasGarden()
    override fun exploreCastle(direction: Direction): Directions =
        tile.exploreCastle(direction.opposite()).map { it.opposite() }

    override fun exploreRoad(direction: Direction): Directions =
        tile.exploreRoad(direction.opposite()).map { it.opposite() }

    override fun hasEmblem(): Boolean = tile.hasEmblem()
}
