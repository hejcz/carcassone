package io.github.hejcz.inn.tiles

import io.github.hejcz.basic.tile.*
import io.github.hejcz.core.*

object TileEA : InnTile, TileWithGreenFields by GreenFields(
    setOf(Location(Up), Location(Right), Location(Down, LeftSide), Location(Left, RightSide)),
    setOf(Location(Down, RightSide), Location(Left, LeftSide))
) {
    override fun isInnOnRoad(direction: Direction): Boolean = direction == Left || direction == Down
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = direction.allIfOneOf(Left, Down)
}

object TileEB : InnTile, TileWithGreenFields by GreenFields(
    setOf(Location(Up), Location(Right, LeftSide), Location(Left, RightSide)),
    setOf(Location(Down), Location(Right, RightSide), Location(Left, LeftSide))
) {
    override fun isInnOnRoad(direction: Direction): Boolean = direction == Left || direction == Right
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = direction.allIfOneOf(Left, Right)
    override fun hasGarden(): Boolean = true
}

object TileEC : InnTile, TileWithGreenFields by GreenFields(
    setOf(Location(Up), Location(Right, LeftSide), Location(Left, RightSide)),
    setOf(Location(Down, RightSide), Location(Left, LeftSide)),
    setOf(Location(Down, LeftSide), Location(Right, RightSide))
) {
    override fun isInnOnRoad(direction: Direction): Boolean = direction == Left || direction == Right
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = direction.sameIfOneOf(Left, Down, Right)
}

object TileED : InnTile, TileWithGreenFields by GreenFields(
    setOf(Location(Up), Location(Right, LeftSide), Location(Left, RightSide)),
    setOf(Location(Down), Location(Right, RightSide), Location(Left, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = direction.sameIfOneOf(Left, Down)
    override fun hasCloister(): Boolean = true
}

object TileEE : InnTile, TileWithGreenFields by GreenFields(
    setOf(Location(Up, LeftSide), Location(Left, RightSide)),
    setOf(Location(Down, LeftSide), Location(Right, RightSide)),
    setOf(Location(Up, RightSide), Location(Right, LeftSide), Location(Down, RightSide), Location(Left, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileEF : InnTile, TileWithGreenFields by GreenFields(
    setOf(Location(Right, LeftSide)),
    setOf(Location(Down), Location(Right, RightSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(Left, Up)
    override fun exploreRoad(direction: Direction): Directions = direction.sameIf(Right)
}

object TileEG : InnTile, TileWithGreenFields by GreenFields(
    setOf(Location(Up)),
    setOf(Location(Down), Location(Right))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.sameIf(Left)
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileEH : InnTile {
    override fun exploreGreenFields(location: Location): Locations = setOf(Location(Center))
    override fun exploreCastle(direction: Direction): Directions = setOf(direction)
    override fun exploreRoad(direction: Direction): Directions = emptySet()
    override fun hasGarden(): Boolean = true
}

object TileEI : InnTile, TileWithGreenFields by GreenFields(
    setOf(Location(Left, RightSide)),
    setOf(Location(Left, LeftSide)),
    setOf(Location(Right, RightSide)),
    setOf(Location(Right, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.sameIfOneOf(Up, Down)

    override fun exploreRoad(direction: Direction): Directions = direction.sameIfOneOf(Left, Right)
}

object TileEJ : InnTile, TileWithGreenFields by GreenFields(
    setOf(Location(Left), Location(Down, RightSide)),
    setOf(Location(Right), Location(Down, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.sameIf(Up)
    override fun exploreRoad(direction: Direction): Directions = direction.sameIfOneOf(Left, Right)
}


object TileEK : InnTile {
    override fun exploreCastle(direction: Direction): Directions = setOf(Up, Down, Left, Right)
    override fun exploreGreenFields(location: Location): Locations = emptySet()
    override fun exploreRoad(direction: Direction): Directions = emptySet()
    override fun hasCathedral(): Boolean = true
}

object TileEL : InnTile, TileWithGreenFields by GreenFields(
    setOf(Location(Right, RightSide), Location(Down, LeftSide)),
    setOf(Location(Right, LeftSide), Location(Down, RightSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(Up, Left )
    override fun exploreRoad(direction: Direction): Directions = direction.allIfOneOf(Right, Down)
    override fun hasEmblem(direction: Direction): Boolean = true
    override fun isInnOnRoad(direction: Direction): Boolean = direction == Right || direction == Down
}


object TileEM : InnTile, TileWithGreenFields by GreenFields(
    setOf(Location(Left, RightSide), Location(Down, LeftSide), Location(Right)),
    setOf(Location(Left, LeftSide), Location(Down, RightSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.sameIf(Up)
    override fun exploreRoad(direction: Direction): Directions = direction.allIfOneOf(Down, Left)
    override fun isInnOnRoad(direction: Direction): Boolean = direction == Down || direction == Left
}


object TileEN : InnTile, TileWithGreenFields by GreenFields(
    setOf(Location(Right), Location(Down, LeftSide)),
    setOf(Location(Down, RightSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(Up, Left)
    override fun exploreRoad(direction: Direction): Directions = direction.sameIf(Down)
    override fun isInnOnRoad(direction: Direction): Boolean = direction == Down
}


object TileEO : InnTile, TileWithGreenFields by GreenFields(
    setOf(Location(Down))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.sameIfOneOf(Up, Left, Right)
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}


object TileEP : InnTile, TileWithGreenFields by GreenFields(
    setOf(Location(Down))
) {
    override fun exploreCastle(direction: Direction): Directions = when (direction) {
        Up, Right -> setOf(Up, Right)
        Left -> setOf(Left)
        else -> emptySet()
    }
    override fun exploreRoad(direction: Direction): Directions = emptySet()
    override fun hasEmblem(direction: Direction): Boolean = direction == Up || direction == Right
}


object TileEQ : InnTile, TileWithGreenFields by GreenFields(
    setOf(Location(Up, LeftSide)),
    setOf(Location(Down, LeftSide)),
    setOf(Location(Up, RightSide)),
    setOf(Location(Down, RightSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(Left, Right)
    override fun exploreRoad(direction: Direction): Directions = direction.sameIfOneOf(Up, Down)
    override fun hasEmblem(direction: Direction): Boolean = true
}

