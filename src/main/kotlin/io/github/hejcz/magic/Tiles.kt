package io.github.hejcz.magic

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*

interface MagicTile : Tile {
    override fun rotate(rotation: Rotation): Tile = when (rotation) {
        NoRotation -> this
        Rotation90 -> MagicRotated90(this)
        Rotation180 -> MagicRotated180(this)
        Rotation270 -> MagicRotated270(this)
    }

    private class MagicRotated90(tile: Tile) : TileRotated90(tile), MagicTile
    private class MagicRotated180(tile: Tile) : TileRotated180(tile), MagicTile
    private class MagicRotated270(tile: Tile) : TileRotated270(tile), MagicTile
}

object MaHeA : MagicTile, TileWithGreenFields by GreenFields(
    setOf(Location(Left, RightSide), Location(Right, LeftSide)),
    setOf(Location(Left, LeftSide), Location(Down, RightSide)),
    setOf(Location(Right, RightSide), Location(Down, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.sameIf(Up)
    override fun exploreRoad(direction: Direction): Directions = direction.sameIfOneOf(Down, Left, Right)
}

object MaHeB : MagicTile, TileWithGreenFields by GreenFields(
    setOf(Location(Down, LeftSide)),
    setOf(Location(Down, RightSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.sameIfOneOf(Up, Left, Right)
    override fun exploreRoad(direction: Direction): Directions = direction.sameIf(Down)
}

object MaHeC : MagicTile, TileWithGreenFields by GreenFields(
    setOf(Location(Down, LeftSide)),
    setOf(Location(Down, RightSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(Up, Left, Right)
    override fun exploreRoad(direction: Direction): Directions = direction.sameIf(Down)
}

object MaHeD : MagicTile, TileWithGreenFields by GreenFields(setOf(Location(Up), Location(Down))) {
    override fun exploreCastle(direction: Direction): Directions = direction.sameIfOneOf(Left, Right)
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object MaHeE : MagicTile, TileWithGreenFields by GreenFields(
    setOf(Location(Down, LeftSide), Location(Right, RightSide)),
    setOf(Location(Down, RightSide), Location(Right, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.sameIfOneOf(Up, Left)
    override fun exploreRoad(direction: Direction): Directions = direction.allIfOneOf(Down, Right)
    override fun hasGarden(): Boolean = true
}

object MaHeF : MagicTile, TileWithGreenFields by GreenFields(
    setOf(Location(Down, LeftSide), Location(Right, RightSide)),
    setOf(Location(Down, RightSide), Location(Right, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(Up, Left)
    override fun exploreRoad(direction: Direction): Directions = direction.allIfOneOf(Down, Right)
    override fun hasGarden(): Boolean = true
}

object MaHeG : MagicTile, TileWithGreenFields by GreenFields(
    setOf(Location(Down, LeftSide)),
    setOf(Location(Down, RightSide)),
    setOf(Location(Up, LeftSide)),
    setOf(Location(Up, RightSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(Right, Left)
    override fun exploreRoad(direction: Direction): Directions = direction.sameIfOneOf(Down, Up)
}

object MaHeH : MagicTile, TileWithGreenFields by GreenFields(
    setOf(Location(Right, LeftSide)),
    setOf(Location(Left, LeftSide), Location(Down, RightSide)),
    setOf(Location(Left, RightSide), Location(Right, RightSide), Location(Down, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(Right, Left)
    override fun exploreRoad(direction: Direction): Directions = direction.sameIfOneOf(Down, Up)
}