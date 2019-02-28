package io.github.hejcz.core

data class Position(val x: Int, val y: Int) {
    fun surrounding(): Set<io.github.hejcz.core.Position> = Position.Companion.adjacent.map { it(x, y) }.toSet()
    fun relativeDirectionTo(other: Position) =
        Position.Companion.relativePosition(Pair(x - other.x, y - other.y))

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
