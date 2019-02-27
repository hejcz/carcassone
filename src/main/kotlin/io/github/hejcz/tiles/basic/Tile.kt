package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.*

interface Tile : GreenFieldExplorable {
    fun exploreCastle(direction: Direction): Collection<Direction>
    fun exploreRoad(direction: Direction): Collection<Direction>
    fun hasGarden(): Boolean = false
    fun hasCloister(): Boolean = false
    fun hasEmblem(): Boolean = false
    fun isValidNeighborFor(neighbor: Tile, direction: Direction): Boolean {
        return exploreCastle(direction).contains(direction) && neighbor.exploreCastle(direction.opposite()).contains(
            direction.opposite()
        )
            || exploreRoad(direction).contains(direction) && neighbor.exploreRoad(direction.opposite()).contains(
            direction.opposite()
        )
            || exploreGreenFields(Location(direction)).contains(Location(direction)) && neighbor.exploreGreenFields(
            Location(direction.opposite())
        ).contains(Location(direction.opposite()))
    }

    fun name() = javaClass.simpleName.substring(4)
    fun rotate(rotation: Rotation): Tile = when (rotation) {
        NoRotation -> this
        Rotation90 -> TileRotated90(this)
        Rotation180 -> TileRotated180(this)
        Rotation270 -> TileRotated270(this)
    }
}
