package io.github.hejcz.core

import io.github.hejcz.basic.tile.*

sealed class Role {
    abstract fun canBePlacedOn(tile: Tile): Boolean
}

data class Knight(val direction: Direction) : Role() {
    override fun canBePlacedOn(tile: Tile) = direction in tile.exploreCastle(direction)
}

data class Brigand(val direction: Direction) : Role() {
    override fun canBePlacedOn(tile: Tile) = direction in tile.exploreRoad(direction)
}

data class Peasant(val location: Location) : Role() {
    override fun canBePlacedOn(tile: Tile) = location in tile.exploreGreenFields(location)
}

object Monk : Role() {
    override fun canBePlacedOn(tile: Tile) = tile.hasCloister()
}

object Abbot : Role() {
    override fun canBePlacedOn(tile: Tile) = tile.hasGarden() || tile.hasCloister()
}
