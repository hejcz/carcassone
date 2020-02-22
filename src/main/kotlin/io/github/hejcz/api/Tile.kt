package io.github.hejcz.api

interface Tile : TileWithGreenFields {
    fun exploreCastle(direction: Direction): Directions
    fun exploreRoad(direction: Direction): Directions
    fun hasGarden(): Boolean = false
    fun hasCloister(): Boolean = false
    fun hasEmblem(direction: Direction): Boolean = false
    fun isValidNeighborFor(other: Tile, direction: Direction): Boolean
    fun name(): String
    fun rotate(rotation: Rotation): Tile
}
