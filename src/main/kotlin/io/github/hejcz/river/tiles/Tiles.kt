package io.github.hejcz.river.tiles

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*

object TileBB6F10 : RiverTile, TileWithGreenFields by GreenFields(
    setOf(Location(Up), Location(Right, LeftSide), Location(Left), Location(Down, RightSide)),
    setOf(Location(Right, RightSide), Location(Down, LeftSide))
) {
    override fun exploreRiver(): Directions = setOf(Down, Right)
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = emptySet()
    override fun hasGarden(): Boolean = true
}

object TileBB6F11 : RiverTile, TileWithGreenFields by GreenFields(
    setOf(Location(Left, RightSide), Location(Up, LeftSide)),
    setOf(Location(Left, LeftSide), Location(Down, RightSide)),
    setOf(Location(Right, RightSide), Location(Down, LeftSide)),
    setOf(Location(Right, LeftSide), Location(Up, RightSide))
) {
    override fun exploreRiver(): Directions = setOf(Down, Up)
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = direction.allIfOneOf(Left, Right)
}

object TileBB6F12 : RiverTile {
    override fun exploreRiver(): Directions = setOf(Down)
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = emptySet()
    override fun exploreGreenFields(location: Location): Locations =
        setOf(Location(Up), Location(Down, RightSide), Location(Down, LeftSide), Location(Left), Location(Right))
}

object TileBB6F1 : RiverTile {
    override fun exploreRiver(): Directions = setOf(Down)
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = emptySet()
    override fun exploreGreenFields(location: Location): Locations =
        setOf(Location(Up), Location(Down, RightSide), Location(Down, LeftSide), Location(Left), Location(Right))
}

object TileBB6F2 : RiverTile, TileWithGreenFields by GreenFields(
    setOf(Location(Left, RightSide)),
    setOf(Location(Left, LeftSide), Location(Down, RightSide)),
    setOf(Location(Right, RightSide), Location(Down, LeftSide)),
    setOf(Location(Right, LeftSide))
) {
    override fun exploreRiver(): Directions = setOf(Left, Right)
    override fun exploreCastle(direction: Direction): Directions = direction.sameIf(Up)
    override fun exploreRoad(direction: Direction): Directions = direction.sameIf(Down)
}

object TileBB6F3 : RiverTile, TileWithGreenFields by GreenFields(
    setOf(Location(Left, RightSide), Location(Right, LeftSide)),
    setOf(Location(Left, LeftSide), Location(Right, RightSide))
) {
    override fun exploreRiver(): Directions = setOf(Left, Right)
    override fun exploreCastle(direction: Direction): Directions = direction.sameIfOneOf(Up, Down)

    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileBB6F4 : RiverTile, TileWithGreenFields by GreenFields(
    setOf(Location(Left, RightSide), Location(Right, LeftSide), Location(Up)),
    setOf(Location(Left, LeftSide), Location(Right, RightSide), Location(Down))
) {
    override fun exploreRiver(): Directions = setOf(Left, Right)
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileBB6F5 : RiverTile, TileWithGreenFields by GreenFields(
    setOf(Location(Left, RightSide), Location(Down, LeftSide)),
    setOf(Location(Left, LeftSide), Location(Down, RightSide))
) {
    override fun exploreRiver(): Directions = setOf(Left, Down)
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(Up, Right)
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileBB6F6 : RiverTile, TileWithGreenFields by GreenFields(
    setOf(Location(Up, LeftSide), Location(Down, RightSide), Location(Left)),
    setOf(Location(Up, RightSide), Location(Down, LeftSide), Location(Right))
) {
    override fun exploreRiver(): Directions = setOf(Up, Down)
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileBB6F7 : RiverTile, TileWithGreenFields by GreenFields(
    setOf(Location(Up, LeftSide), Location(Left, RightSide)),
    setOf(Location(Up, RightSide), Location(Down), Location(Left, LeftSide), Location(Right))
) {
    override fun exploreRiver(): Directions = setOf(Up, Left)
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileBB6F8 : RiverTile, TileWithGreenFields by GreenFields(
    setOf(Location(Up), Location(Left, RightSide), Location(Right, LeftSide)),
    setOf(Location(Left, LeftSide)),
    setOf(Location(Right, RightSide))
) {
    override fun exploreRiver(): Directions = setOf(Left, Right)
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = direction.sameIf(Down)
    override fun hasCloister(): Boolean = true
}

object TileBB6F9 : RiverTile, TileWithGreenFields by GreenFields(
    setOf(Location(Up, LeftSide), Location(Left, RightSide)),
    setOf(Location(Up, RightSide), Location(Right, LeftSide), Location(Left, LeftSide), Location(Down, RightSide)),
    setOf(Location(Right, RightSide), Location(Down, LeftSide))
) {
    override fun exploreRiver(): Directions = setOf(Down, Right)
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = direction.allIfOneOf(Up, Left)
}
