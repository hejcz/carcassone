package io.github.hejcz.core

typealias Directions = Collection<Direction>

sealed class Direction {
    abstract fun opposite(): Direction
    abstract fun left(): Direction
    abstract fun right(): Direction
    abstract fun move(position: Position): Position
}

object Up : Direction() {
    override fun opposite(): Direction = Down
    override fun left(): Direction = Left
    override fun right(): Direction = Right
    override fun move(position: Position): Position =
        Position(position.x, position.y + 1)
}

object Down : Direction() {
    override fun opposite(): Direction = Up
    override fun left(): Direction = Right
    override fun right(): Direction = Left
    override fun move(position: Position): Position =
        Position(position.x, position.y - 1)
}

object Left : Direction() {
    override fun opposite(): Direction = Right
    override fun left(): Direction = Down
    override fun right(): Direction = Up
    override fun move(position: Position): Position =
        Position(position.x - 1, position.y)
}

object Right : Direction() {
    override fun opposite(): Direction = Left
    override fun left(): Direction = Up
    override fun right(): Direction = Down
    override fun move(position: Position): Position =
        Position(position.x + 1, position.y)
}

object Center : Direction() {
    override fun opposite(): Direction = Center
    override fun left(): Direction = Center
    override fun right(): Direction = Center
    override fun move(position: Position): Position = position
}
