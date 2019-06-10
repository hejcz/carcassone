package io.github.hejcz.basic.tiles

import io.github.hejcz.core.*

object TileA : Tile {
    override fun hasCloister(): Boolean = true
    override fun exploreCastle(direction: Direction) = emptySet<Direction>()
    override fun exploreGreenFields(location: Location): Locations =
        setOf(Location(Right), Location(Up), Location(Left), Location(Down, RightSide), Location(Down, LeftSide))

    override fun exploreRoad(direction: Direction): Directions = setOf(Down)
}

object TileB : Tile {
    override fun hasCloister(): Boolean = true
    override fun exploreCastle(direction: Direction) = emptySet<Direction>()
    override fun exploreGreenFields(location: Location): Locations =
        setOf(Location(Right), Location(Up), Location(Left), Location(Down))

    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}

object TileC : Tile {
    override fun hasEmblem(): Boolean = true
    override fun exploreCastle(direction: Direction) = setOf(Up, Down, Left, Right)
    override fun exploreGreenFields(location: Location) = emptySet<Location>()
    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}

object TileD : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Left, RightSide), Location(Right, LeftSide)),
    setOf(Location(Left, LeftSide), Location(Right, RightSide), Location(Down))
) {
    override fun exploreCastle(direction: Direction): Directions = setOf(Up)
    override fun exploreRoad(direction: Direction): Directions = setOf(Left, Right)
}

object TileE : Tile {
    override fun exploreGreenFields(location: Location): Locations =
        setOf(Location(Left), Location(Down), Location(Right))

    override fun exploreCastle(direction: Direction): Directions = setOf(Up)
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileEWithGarden : Tile by TileWithGarden(TileE)

object TileF : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Up)),
    setOf(Location(Down))
) {
    override fun exploreCastle(direction: Direction): Directions = setOf(Left, Right)

    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
    override fun hasEmblem(): Boolean = true
}

object TileG : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Up)),
    setOf(Location(Down))
) {
    override fun exploreCastle(direction: Direction): Directions = setOf(Left, Right)

    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}

object TileH : Tile {
    override fun exploreGreenFields(location: Location): Locations = setOf(Location(Up), Location(Down))

    override fun exploreCastle(direction: Direction): Directions = when (direction) {
        Left, Right -> setOf(direction)
        else -> emptySet()
    }

    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}

object TileHWithGarden : Tile by TileWithGarden(TileH)

object TileI : Tile {
    override fun exploreGreenFields(location: Location): Locations = setOf(Location(Left), Location(Down))

    override fun exploreCastle(direction: Direction): Directions = when (direction) {
        Up, Left -> setOf(direction)
        else -> emptySet()
    }

    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}

object TileIWithGarden : Tile by TileWithGarden(TileI)

object TileJ : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Left), Location(Right, LeftSide), Location(Down, RightSide)),
    setOf(Location(Right, RightSide), Location(Down, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Directions = setOf(Up)
    override fun exploreRoad(direction: Direction): Directions = setOf(Down, Right)
}

object TileK : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Left, RightSide), Location(Right), Location(Down, LeftSide)),
    setOf(Location(Left, LeftSide), Location(Down, RightSide))
) {
    override fun exploreCastle(direction: Direction): Directions = setOf(Up)
    override fun exploreRoad(direction: Direction): Directions = setOf(Down, Left)
}

object TileL : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Left, RightSide), Location(Right, LeftSide)),
    setOf(Location(Left, LeftSide), Location(Down, RightSide)),
    setOf(Location(Right, RightSide), Location(Down, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Directions = setOf(Up)
    override fun exploreRoad(direction: Direction): Directions = when (direction) {
        Left, Right, Down -> setOf(direction)
        else -> emptySet()
    }
}

object TileM : Tile {
    override fun exploreGreenFields(location: Location): Locations = setOf(Location(Left), Location(Down))

    override fun exploreCastle(direction: Direction): Directions = setOf(Up, Right)

    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
    override fun hasEmblem(): Boolean = true
}

object TileMWithGarden : Tile by TileWithGarden(TileM)

object TileN : Tile {
    override fun exploreGreenFields(location: Location): Locations = setOf(Location(Left), Location(Down))

    override fun exploreCastle(direction: Direction): Directions = setOf(Up, Right)

    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}

object TileNWithGarden : Tile by TileWithGarden(TileN)

object TileO : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Down, RightSide), Location(Right, LeftSide)),
    setOf(Location(Down, LeftSide), Location(Right, RightSide))
) {
    override fun hasEmblem(): Boolean = true
    override fun exploreCastle(direction: Direction): Directions = setOf(Up, Left)
    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}

object TileP : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Down, RightSide), Location(Right, LeftSide)),
    setOf(Location(Down, LeftSide), Location(Right, RightSide))
) {
    override fun exploreCastle(direction: Direction): Directions = setOf(Up, Left)
    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}

object TileQ : Tile {
    override fun hasEmblem(): Boolean = true

    override fun exploreGreenFields(location: Location): Locations = setOf(Location(Down))

    override fun exploreCastle(direction: Direction): Directions = setOf(Up, Left, Right)

    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}

object TileR : Tile {
    override fun exploreGreenFields(location: Location): Locations = setOf(Location(Down))

    override fun exploreCastle(direction: Direction): Directions = setOf(Up, Left, Right)

    override fun exploreRoad(direction: Direction) = emptySet<Direction>()
}

object TileRWithGarden : Tile by TileWithGarden(TileR)

object TileS : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Down, RightSide)),
    setOf(Location(Down, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Directions = setOf(Up, Left, Right)

    override fun exploreRoad(direction: Direction): Directions = setOf(Down)
    override fun hasEmblem(): Boolean = true
}

object TileT : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Down, RightSide)),
    setOf(Location(Down, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Directions = setOf(Up, Left, Right)

    override fun exploreRoad(direction: Direction): Directions = setOf(Down)
}

object TileU : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Left), Location(Up, LeftSide), Location(Down, RightSide)),
    setOf(Location(Right), Location(Up, RightSide), Location(Down, LeftSide))
) {
    override fun exploreCastle(direction: Direction) = emptySet<Direction>()
    override fun exploreRoad(direction: Direction) = setOf(Down, Up)
}

object TileUWithGarden : Tile by TileWithGarden(TileU)

object TileV : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Left, LeftSide), Location(Down, RightSide)),
    setOf(Location(Left, RightSide), Location(Down, LeftSide), Location(Up), Location(Right))
) {
    override fun exploreCastle(direction: Direction) = emptySet<Direction>()
    override fun exploreRoad(direction: Direction) = setOf(Down, Left)
}

object TileVWithGarden : Tile by TileWithGarden(TileV)

object TileW : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Left, RightSide), Location(Right, LeftSide), Location(Up)),
    setOf(Location(Left, LeftSide), Location(Down, RightSide)),
    setOf(Location(Right, RightSide), Location(Down, LeftSide))
) {
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = when (direction) {
        Left, Right, Down -> setOf(direction)
        else -> emptySet()
    }
}

object TileX : Tile, GreenFieldExplorable by RegionGreenFieldExplorable(
    setOf(Location(Left, RightSide), Location(Up, LeftSide)),
    setOf(Location(Left, LeftSide), Location(Down, RightSide)),
    setOf(Location(Right, RightSide), Location(Down, LeftSide)),
    setOf(Location(Right, LeftSide), Location(Up, RightSide))
) {
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = setOf(direction)
}
