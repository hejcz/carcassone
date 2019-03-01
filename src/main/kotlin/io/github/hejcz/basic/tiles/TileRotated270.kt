package io.github.hejcz.basic.tiles

import io.github.hejcz.core.*

open class TileRotated270(private val tile: Tile) : Tile {
    override fun exploreGreenFields(location: Location): Locations =
        tile.exploreGreenFields(location.right()).map { it.left() }

    override fun hasCloister(): Boolean = tile.hasCloister()
    override fun hasGarden(): Boolean = tile.hasGarden()
    override fun exploreCastle(direction: Direction): Directions =
        tile.exploreCastle(direction.right()).map { it.left() }

    override fun exploreRoad(direction: Direction): Directions =
        tile.exploreRoad(direction.right()).map { it.left() }

    override fun hasEmblem(): Boolean = tile.hasEmblem()
}
