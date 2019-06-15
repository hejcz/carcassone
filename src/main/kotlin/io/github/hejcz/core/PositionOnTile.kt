package io.github.hejcz.core

typealias Directions = Collection<Direction>

sealed class Direction {
    abstract fun opposite(): Direction
    abstract fun left(): Direction
    abstract fun right(): Direction
    abstract fun move(position: Position): Position
    fun sameIf(direction: Direction) = if (direction == this) setOf(this) else emptySet()
    fun sameIfOneOf(vararg directions: Direction) = if (this in directions) setOf(this) else emptySet()
    fun allIfOneOf(vararg directions: Direction) = if (this in directions) directions.toSet() else emptySet()
}

object Up : Direction() {
    override fun opposite(): Direction = Down
    override fun left(): Direction = Left
    override fun right(): Direction = Right
    override fun move(position: Position): Position = Position(position.x, position.y + 1)
}

object Down : Direction() {
    override fun opposite(): Direction = Up
    override fun left(): Direction = Right
    override fun right(): Direction = Left
    override fun move(position: Position): Position = Position(position.x, position.y - 1)
}

object Left : Direction() {
    override fun opposite(): Direction = Right
    override fun left(): Direction = Down
    override fun right(): Direction = Up
    override fun move(position: Position): Position = Position(position.x - 1, position.y)
}

object Right : Direction() {
    override fun opposite(): Direction = Left
    override fun left(): Direction = Up
    override fun right(): Direction = Down
    override fun move(position: Position): Position = Position(position.x + 1, position.y)
}

object Center : Direction() {
    override fun opposite(): Direction = Center
    override fun left(): Direction = Center
    override fun right(): Direction = Center
    override fun move(position: Position): Position = position
}

data class Position(val x: Int, val y: Int) {
    fun surrounding(): Set<Position> = adjacent.map { it(x, y) }.toSet()
    fun relativeDirectionTo(other: Position) = relativePosition(Pair(x - other.x, y - other.y))

    companion object {
        private fun relativePosition(diff: Pair<Int, Int>) = when (diff) {
            Pair(0, 1) -> Up
            Pair(0, -1) -> Down
            Pair(1, 0) -> Right
            Pair(-1, 0) -> Left
            else -> throw RuntimeException("tiles are not adjacent")
        }

        private val adjacent: Set<(Int, Int) -> Position> =
            setOf(
                { x: Int, y: Int -> Position(x - 1, y) },
                { x: Int, y: Int -> Position(x - 1, y - 1) },
                { x: Int, y: Int -> Position(x - 1, y + 1) },
                { x: Int, y: Int -> Position(x, y + 1) },
                { x: Int, y: Int -> Position(x, y - 1) },
                { x: Int, y: Int -> Position(x + 1, y) },
                { x: Int, y: Int -> Position(x + 1, y - 1) },
                { x: Int, y: Int -> Position(x + 1, y + 1) }
            )
    }
}

typealias Locations = Collection<Location>

data class Location(val direction: Direction, val side: Side?) {
    constructor(direction: Direction) : this(direction, null)

    fun left(): Location = Location(direction.left(), side)
    fun right(): Location = Location(direction.right(), side)
    fun mirrored(): Location = Location(direction.opposite(), side?.opposite())
    fun opposite(): Location = Location(direction.opposite(), side)
}

/**
 * Represents side of the road that is required for valid green field discovery.
 */
sealed class Side {
    abstract fun opposite(): Side
}

object LeftSide : Side() {
    override fun opposite(): Side = RightSide
}

object RightSide : Side() {
    override fun opposite(): Side = LeftSide
}
