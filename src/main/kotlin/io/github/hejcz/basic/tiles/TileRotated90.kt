package io.github.hejcz.basic.tiles

import io.github.hejcz.core.*

open class TileRotated90(private val tile: Tile) : Tile {
    override fun exploreGreenFields(location: Location): Locations =
        tile.exploreGreenFields(location.left()).map { it.right() }

    override fun hasCloister(): Boolean = tile.hasCloister()
    override fun hasGarden(): Boolean = tile.hasGarden()
    override fun exploreCastle(direction: Direction): Directions =
        tile.exploreCastle(direction.left()).map { it.right() }

    override fun exploreRoad(direction: Direction): Directions =
        tile.exploreRoad(direction.left()).map { it.right() }

    override fun hasEmblem(): Boolean = tile.hasEmblem()
}
