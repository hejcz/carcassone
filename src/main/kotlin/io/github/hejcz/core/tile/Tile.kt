package io.github.hejcz.core.tile

import io.github.hejcz.core.*

interface Tile : TileWithGreenFields {
    fun exploreCastle(direction: Direction): Directions
    fun exploreRoad(direction: Direction): Directions
    fun hasGarden(): Boolean = false
    fun hasCloister(): Boolean = false
    fun hasEmblem(direction: Direction): Boolean = false
    fun isValidNeighborFor(other: Tile, direction: Direction): Boolean =
        direction in exploreCastle(direction) && direction.opposite() in other.exploreCastle(direction.opposite()) ||
            direction in exploreRoad(direction) && direction.opposite() in other.exploreRoad(direction.opposite()) ||
            Location(direction) in exploreGreenFields(Location(direction)) && Location(
            direction.opposite()
        ) in other.exploreGreenFields(Location(direction.opposite()))

    fun name() = javaClass.simpleName.substring(4)
    fun rotate(rotation: Rotation): Tile = when (rotation) {
        NoRotation -> this
        Rotation90 -> TileRotated90(this)
        Rotation180 -> TileRotated180(this)
        Rotation270 -> TileRotated270(this)
    }
}
