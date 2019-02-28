package io.github.hejcz.core

import io.github.hejcz.basic.tiles.Tile

sealed class PieceRole {
    abstract fun canBePlacedOn(tile: Tile): Boolean
}

data class Knight(val direction: Direction) : PieceRole() {
    override fun canBePlacedOn(tile: Tile): Boolean = tile.exploreCastle(direction).contains(direction)
}

data class Brigand(val direction: Direction) : PieceRole() {
    override fun canBePlacedOn(tile: Tile): Boolean = tile.exploreRoad(direction).contains(direction)
}

data class Peasant(val location: Location) : PieceRole() {
    override fun canBePlacedOn(tile: Tile): Boolean = tile.exploreGreenFields(location).contains(location)
}

object Monk : PieceRole() {
    override fun canBePlacedOn(tile: Tile): Boolean = tile.hasCloister()
}

object Abbot : PieceRole() {
    override fun canBePlacedOn(tile: Tile): Boolean = tile.hasGarden() || tile.hasCloister()
}
