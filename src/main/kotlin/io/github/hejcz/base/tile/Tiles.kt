package io.github.hejcz.base.tile

import io.github.hejcz.api.*

object TileA : BasicTile {
    override fun hasCloister(): Boolean = true
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreGreenFields(location: Location): Locations =
        setOf(
            Location(Right),
            Location(Up),
            Location(Left),
            Location(
                Down, RightSide
            ), Location(
                Down, LeftSide
            )
        )

    override fun exploreRoad(direction: Direction): Directions = direction.sameIf(
        Down
    )
}

object TileB : BasicTile {
    override fun hasCloister(): Boolean = true
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreGreenFields(location: Location): Locations =
        setOf(
            Location(Right),
            Location(Up),
            Location(Left),
            Location(Down)
        )

    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileC : BasicTile {
    override fun hasEmblem(direction: Direction): Boolean = true
    override fun exploreCastle(direction: Direction) = setOf(
        Up, Down,
        Left, Right
    )
    override fun exploreGreenFields(location: Location) = emptySet<Location>()
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileD : BasicTile, TileWithGreenFields by GreenFields(
    setOf(
        Location(
            Left, RightSide
        ), Location(
            Right, LeftSide
        )
    ),
    setOf(
        Location(
            Left, LeftSide
        ), Location(
            Right, RightSide
        ), Location(Down)
    )
) {
    override fun exploreCastle(direction: Direction): Directions = direction.sameIf(
        Up
    )
    override fun exploreRoad(direction: Direction): Directions = direction.allIfOneOf(
        Left, Right
    )
}

object TileE : BasicTile {
    override fun exploreGreenFields(location: Location): Locations =
        setOf(
            Location(Left),
            Location(Down),
            Location(Right)
        )

    override fun exploreCastle(direction: Direction): Directions = direction.sameIf(
        Up
    )
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileEWithGarden : BasicTile by TileWithGarden(TileE)

object TileF : BasicTile, TileWithGreenFields by GreenFields(
    setOf(Location(Up)),
    setOf(Location(Down))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(
        Left, Right
    )
    override fun exploreRoad(direction: Direction): Directions = emptySet()
    override fun hasEmblem(direction: Direction): Boolean = true
}

object TileG : BasicTile, TileWithGreenFields by GreenFields(
    setOf(Location(Up)),
    setOf(Location(Down))
) {
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(
        Left, Right
    )
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileH : BasicTile {
    override fun exploreGreenFields(location: Location): Locations = setOf(
        Location(Up),
        Location(Down)
    )
    override fun exploreCastle(direction: Direction): Directions = direction.sameIfOneOf(
        Left, Right
    )
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileHWithGarden : BasicTile by TileWithGarden(TileH)

object TileI : BasicTile {
    override fun exploreGreenFields(location: Location): Locations = setOf(
        Location(Right),
        Location(Down)
    )
    override fun exploreCastle(direction: Direction): Directions = direction.sameIfOneOf(
        Up, Left
    )
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileIWithGarden : BasicTile by TileWithGarden(TileI)

object TileJ : BasicTile, TileWithGreenFields by GreenFields(
    setOf(
        Location(Left),
        Location(
            Right, LeftSide
        ), Location(
            Down, RightSide
        )
    ),
    setOf(
        Location(
            Right, RightSide
        ), Location(
            Down, LeftSide
        )
    )
) {
    override fun exploreCastle(direction: Direction): Directions = direction.sameIf(
        Up
    )
    override fun exploreRoad(direction: Direction): Directions = setOf(
        Down, Right
    )
}

object TileK : BasicTile, TileWithGreenFields by GreenFields(
    setOf(
        Location(
            Left, RightSide
        ), Location(Right),
        Location(
            Down, LeftSide
        )
    ),
    setOf(
        Location(
            Left, LeftSide
        ), Location(
            Down, RightSide
        )
    )
) {
    override fun exploreCastle(direction: Direction): Directions = direction.sameIf(
        Up
    )
    override fun exploreRoad(direction: Direction): Directions = setOf(
        Down, Left
    )
}

object TileL : BasicTile, TileWithGreenFields by GreenFields(
    setOf(
        Location(
            Left, RightSide
        ), Location(
            Right, LeftSide
        )
    ),
    setOf(
        Location(
            Left, LeftSide
        ), Location(
            Down, RightSide
        )
    ),
    setOf(
        Location(
            Right, RightSide
        ), Location(
            Down, LeftSide
        )
    )
) {
    override fun exploreCastle(direction: Direction): Directions = direction.sameIf(
        Up
    )
    override fun exploreRoad(direction: Direction): Directions = direction.sameIfOneOf(
        Left, Right,
        Down
    )
}

object TileM : BasicTile {
    override fun exploreGreenFields(location: Location): Locations = setOf(
        Location(Left),
        Location(Down)
    )
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(
        Up, Right
    )
    override fun exploreRoad(direction: Direction): Directions = emptySet()
    override fun hasEmblem(direction: Direction): Boolean = true
}

object TileMWithGarden : BasicTile by TileWithGarden(TileM)

object TileN : BasicTile {
    override fun exploreGreenFields(location: Location): Locations = setOf(
        Location(Left),
        Location(Down)
    )
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(
        Up, Right
    )
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileNWithGarden : BasicTile by TileWithGarden(TileN)

object TileO : BasicTile, TileWithGreenFields by GreenFields(
    setOf(
        Location(
            Down, RightSide
        ), Location(
            Right, LeftSide
        )
    ),
    setOf(
        Location(
            Down, LeftSide
        ), Location(
            Right, RightSide
        )
    )
) {
    override fun hasEmblem(direction: Direction): Boolean = true
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(
        Up, Left
    )
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileP : BasicTile, TileWithGreenFields by GreenFields(
    setOf(
        Location(
            Down, RightSide
        ), Location(
            Right, LeftSide
        )
    ),
    setOf(
        Location(
            Down, LeftSide
        ), Location(
            Right, RightSide
        )
    )
) {
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(
        Up, Left
    )
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileQ : BasicTile {
    override fun hasEmblem(direction: Direction): Boolean = true
    override fun exploreGreenFields(location: Location): Locations = setOf(
        Location(Down)
    )
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(
        Up, Left,
        Right
    )
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileR : BasicTile {
    override fun exploreGreenFields(location: Location): Locations = setOf(
        Location(Down)
    )
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(
        Up, Left,
        Right
    )
    override fun exploreRoad(direction: Direction): Directions = emptySet()
}

object TileRWithGarden : BasicTile by TileWithGarden(TileR)

object TileS : BasicTile, TileWithGreenFields by GreenFields(
    setOf(
        Location(
            Down, RightSide
        )
    ),
    setOf(
        Location(
            Down, LeftSide
        )
    )
) {
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(
        Up, Left,
        Right
    )
    override fun exploreRoad(direction: Direction): Directions = direction.sameIf(
        Down
    )
    override fun hasEmblem(direction: Direction): Boolean = true
}

object TileT : BasicTile, TileWithGreenFields by GreenFields(
    setOf(
        Location(
            Down, RightSide
        )
    ),
    setOf(
        Location(
            Down, LeftSide
        )
    )
) {
    override fun exploreCastle(direction: Direction): Directions = direction.allIfOneOf(
        Up, Left,
        Right
    )
    override fun exploreRoad(direction: Direction): Directions = direction.sameIf(
        Down
    )
}

object TileU : BasicTile, TileWithGreenFields by GreenFields(
    setOf(
        Location(Left),
        Location(
            Up, LeftSide
        ), Location(
            Down, RightSide
        )
    ),
    setOf(
        Location(Right),
        Location(
            Up, RightSide
        ), Location(
            Down, LeftSide
        )
    )
) {
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction) = direction.allIfOneOf(
        Down, Up
    )
}

object TileUWithGarden : BasicTile by TileWithGarden(TileU)

object TileV : BasicTile, TileWithGreenFields by GreenFields(
    setOf(
        Location(
            Left, LeftSide
        ), Location(
            Down, RightSide
        )
    ),
    setOf(
        Location(
            Left, RightSide
        ), Location(
            Down, LeftSide
        ), Location(Up),
        Location(Right)
    )
) {
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction) = direction.allIfOneOf(
        Down, Left
    )
}

object TileVWithGarden : BasicTile by TileWithGarden(TileV)

object TileW : BasicTile, TileWithGreenFields by GreenFields(
    setOf(
        Location(
            Left, RightSide
        ), Location(
            Right, LeftSide
        ), Location(Up)
    ),
    setOf(
        Location(
            Left, LeftSide
        ), Location(
            Down, RightSide
        )
    ),
    setOf(
        Location(
            Right, RightSide
        ), Location(
            Down, LeftSide
        )
    )
) {
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = direction.sameIfOneOf(
        Left, Right,
        Down
    )
}

object TileX : BasicTile, TileWithGreenFields by GreenFields(
    setOf(
        Location(
            Left, RightSide
        ), Location(
            Up, LeftSide
        )
    ),
    setOf(
        Location(
            Left, LeftSide
        ), Location(
            Down, RightSide
        )
    ),
    setOf(
        Location(
            Right, RightSide
        ), Location(
            Down, LeftSide
        )
    ),
    setOf(
        Location(
            Right, LeftSide
        ), Location(
            Up, RightSide
        )
    )
) {
    override fun exploreCastle(direction: Direction): Directions = emptySet()
    override fun exploreRoad(direction: Direction): Directions = setOf(direction)
}
