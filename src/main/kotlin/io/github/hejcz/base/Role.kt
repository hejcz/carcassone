package io.github.hejcz.base

import io.github.hejcz.api.Direction
import io.github.hejcz.api.Location
import io.github.hejcz.api.Role
import io.github.hejcz.api.Tile

data class Knight(val direction: Direction) : Role {
    override fun canBePlacedOn(tile: Tile) = direction in tile.exploreCastle(direction)
}

data class Brigand(val direction: Direction) : Role {
    override fun canBePlacedOn(tile: Tile) = direction in tile.exploreRoad(direction)
}

data class Peasant(val location: Location) : Role {
    override fun canBePlacedOn(tile: Tile) = location in tile.exploreGreenFields(location)
}

object Monk : Role {
    override fun canBePlacedOn(tile: Tile) = tile.hasCloister()
}

object Abbot : Role {
    override fun canBePlacedOn(tile: Tile) = tile.hasGarden() || tile.hasCloister()
}
