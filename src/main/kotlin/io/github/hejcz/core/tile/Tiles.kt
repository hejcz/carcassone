package io.github.hejcz.core.tile

import io.github.hejcz.core.*

object TileA : Tile {
    override fun hasCloister(): Boolean = true
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreGreenFields(location: Location): Locations =
        setOf(Location(Right), Location(Up), Location(Left), Location(Down, RightSide), Location(Down, LeftSide))
    override fun exploreRoad(direction: Direction): Directions = direction.sameIf(Down)
}

object TileB : Tile {
    override fun hasCloister(): Boolean = true
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreGreenFields(location: Location): Locations =
        setOf(Location(Right), Location(Up), Location(Left), Location(Down))
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileC : Tile {
    override fun hasEmblem(direction: Direction): Boolean = true
    override fun exploreCastle(direction: Direction) = setOf(Up, Down, Left, Right)
    override fun exploreGreenFields(location: Location) = emptySet<Location>()
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileD : Tile, TileWithGreenFields by GreenFields(
    setOf(Location(Left, RightSide), Location(Right, LeftSide)),
    setOf(Location(Left, LeftSide), Location(Right, RightSide), Location(Down))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.sameIf(Up)
    override fun exploreRoad(direction: Direction): Directions = direction.allIfOneOf(Left, Right)
}

object TileE : Tile {
    override fun exploreGreenFields(location: Location): Locations =
        setOf(Location(Left), Location(Down), Location(Right))
    override fun exploreCastle(direction: Direction): Directions = direction.sameIf(Up)
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileEWithGarden : Tile by TileWithGarden(TileE)

object TileF : Tile, TileWithGreenFields by GreenFields(
    setOf(Location(Up)),
    setOf(Location(Down))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(Left, Right)
    override fun exploreRoad(direction: Direction): Directions = emptySet()
    override fun hasEmblem(direction: Direction): Boolean = true
}

object TileG : Tile, TileWithGreenFields by GreenFields(
    setOf(Location(Up)),
    setOf(Location(Down))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(Left, Right)
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileH : Tile {
    override fun exploreGreenFields(location: Location): Locations = setOf(Location(Up), Location(Down))
    override fun exploreCastle(direction: Direction): Directions = direction.sameIfOneOf(Left, Right)
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileHWithGarden : Tile by TileWithGarden(TileH)

object TileI : Tile {
    override fun exploreGreenFields(location: Location): Locations = setOf(Location(Right), Location(Down))
    override fun exploreCastle(direction: Direction): Directions = direction.sameIfOneOf(Up, Left)
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileIWithGarden : Tile by TileWithGarden(TileI)

object TileJ : Tile, TileWithGreenFields by GreenFields(
    setOf(Location(Left), Location(Right, LeftSide), Location(Down, RightSide)),
    setOf(Location(Right, RightSide), Location(Down, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.sameIf(Up)
    override fun exploreRoad(direction: Direction): Directions = setOf(Down, Right)
}

object TileK : Tile, TileWithGreenFields by GreenFields(
    setOf(Location(Left, RightSide), Location(Right), Location(Down, LeftSide)),
    setOf(Location(Left, LeftSide), Location(Down, RightSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.sameIf(Up)
    override fun exploreRoad(direction: Direction): Directions = setOf(Down, Left)
}

object TileL : Tile, TileWithGreenFields by GreenFields(
    setOf(Location(Left, RightSide), Location(Right, LeftSide)),
    setOf(Location(Left, LeftSide), Location(Down, RightSide)),
    setOf(Location(Right, RightSide), Location(Down, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.sameIf(Up)
    override fun exploreRoad(direction: Direction): Directions = direction.sameIfOneOf(Left, Right, Down)
}

object TileM : Tile {
    override fun exploreGreenFields(location: Location): Locations = setOf(Location(Left), Location(Down))
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(Up, Right)
    override fun exploreRoad(direction: Direction): Directions = emptySet()
    override fun hasEmblem(direction: Direction): Boolean = true
}

object TileMWithGarden : Tile by TileWithGarden(TileM)

object TileN : Tile {
    override fun exploreGreenFields(location: Location): Locations = setOf(Location(Left), Location(Down))
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(Up, Right)
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileNWithGarden : Tile by TileWithGarden(TileN)

object TileO : Tile, TileWithGreenFields by GreenFields(
    setOf(Location(Down, RightSide), Location(Right, LeftSide)),
    setOf(Location(Down, LeftSide), Location(Right, RightSide))
) {
    override fun hasEmblem(direction: Direction): Boolean = true
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(Up, Left)
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileP : Tile, TileWithGreenFields by GreenFields(
    setOf(Location(Down, RightSide), Location(Right, LeftSide)),
    setOf(Location(Down, LeftSide), Location(Right, RightSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(Up, Left)
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileQ : Tile {
    override fun hasEmblem(direction: Direction): Boolean = true
    override fun exploreGreenFields(location: Location): Locations = setOf(Location(Down))
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(Up, Left, Right)
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileR : Tile {
    override fun exploreGreenFields(location: Location): Locations = setOf(Location(Down))
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(Up, Left, Right)
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileRWithGarden : Tile by TileWithGarden(TileR)

object TileS : Tile, TileWithGreenFields by GreenFields(
    setOf(Location(Down, RightSide)),
    setOf(Location(Down, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(Up, Left, Right)
    override fun exploreRoad(direction: Direction): Directions = direction.sameIf(Down)
    override fun hasEmblem(direction: Direction): Boolean = true
}

object TileT : Tile, TileWithGreenFields by GreenFields(
    setOf(Location(Down, RightSide)),
    setOf(Location(Down, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(Up, Left, Right)
    override fun exploreRoad(direction: Direction): Directions = direction.sameIf(Down)
}

object TileU : Tile, TileWithGreenFields by GreenFields(
    setOf(Location(Left), Location(Up, LeftSide), Location(Down, RightSide)),
    setOf(Location(Right), Location(Up, RightSide), Location(Down, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction) = direction.allIfOneOf(Down, Up)
}

object TileUWithGarden : Tile by TileWithGarden(TileU)

object TileV : Tile, TileWithGreenFields by GreenFields(
    setOf(Location(Left, LeftSide), Location(Down, RightSide)),
    setOf(Location(Left, RightSide), Location(Down, LeftSide), Location(Up), Location(Right))
) {
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction) = direction.allIfOneOf(Down, Left)
}

object TileVWithGarden : Tile by TileWithGarden(TileV)

object TileW : Tile, TileWithGreenFields by GreenFields(
    setOf(Location(Left, RightSide), Location(Right, LeftSide), Location(Up)),
    setOf(Location(Left, LeftSide), Location(Down, RightSide)),
    setOf(Location(Right, RightSide), Location(Down, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = direction.sameIfOneOf(Left, Right, Down)
}

object TileX : Tile, TileWithGreenFields by GreenFields(
    setOf(Location(Left, RightSide), Location(Up, LeftSide)),
    setOf(Location(Left, LeftSide), Location(Down, RightSide)),
    setOf(Location(Right, RightSide), Location(Down, LeftSide)),
    setOf(Location(Right, LeftSide), Location(Up, RightSide))
) {
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = setOf(direction)
}
