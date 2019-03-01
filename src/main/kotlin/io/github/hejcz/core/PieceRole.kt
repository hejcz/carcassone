package io.github.hejcz.core

import io.github.hejcz.basic.tiles.*

sealed class PieceRole {
    abstract fun canBePlacedOn(tile: Tile): Boolean
}

data class Knight(val direction: Direction) : PieceRole() {
    override fun canBePlacedOn(tile: Tile) = direction in tile.exploreCastle(direction)
}

data class Brigand(val direction: Direction) : PieceRole() {
    override fun canBePlacedOn(tile: Tile) = direction in tile.exploreRoad(direction)
}

data class Peasant(val location: Location) : PieceRole() {
    override fun canBePlacedOn(tile: Tile) = location in tile.exploreGreenFields(location)
}

object Monk : PieceRole() {
    override fun canBePlacedOn(tile: Tile) = tile.hasCloister()
}

object Abbot : PieceRole() {
    override fun canBePlacedOn(tile: Tile) = tile.hasGarden() || tile.hasCloister()
}
