package io.github.hejcz.base.tile

import io.github.hejcz.api.*

interface BasicTile : Tile {
    override fun isValidNeighborFor(other: Tile, direction: Direction): Boolean =
        direction in exploreCastle(direction) && direction.opposite() in other.exploreCastle(direction.opposite()) ||
            direction in exploreRoad(direction) && direction.opposite() in other.exploreRoad(direction.opposite()) ||
            Location(direction) in exploreGreenFields(
            Location(direction)
        ) && Location(
            direction.opposite()
        ) in other.exploreGreenFields(Location(direction.opposite()))

    override fun name() = javaClass.simpleName

    override fun rotate(rotation: Rotation): Tile = when (rotation) {
        NoRotation -> this
        Rotation90 -> TileRotated90(
            this
        )
        Rotation180 -> TileRotated180(
            this
        )
        Rotation270 -> TileRotated270(
            this
        )
    }
}