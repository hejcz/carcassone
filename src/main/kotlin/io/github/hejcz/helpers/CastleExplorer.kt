package io.github.hejcz.helpers

import io.github.hejcz.basic.tiles.*
import io.github.hejcz.core.*

class CastleExplorer(private val state: State, private val initial: PositionedDirection) {
    private val visited: MutableSet<PositionedDirection> = mutableSetOf()
    private var isCompleted = true

    @Synchronized
    fun explore() = when {
        visited.isNotEmpty() -> throw IllegalStateException("Castle explorer should not be reused")
        else -> {
            doExplore(initial)
        }
    }

    fun isCompleted() = isCompleted

    fun positions() = visited.map { it.position }.distinct()

    fun parts() = when {
        visited.isEmpty() -> throw IllegalStateException("Can't access explored castle parts before calling explore method")
        else -> visited.toSet()
    }

    private fun doExplore(current: PositionedDirection) {
        if (current in visited) {
            return
        }
        val tile = state.tileAt(current.position)
        if (tile is NoTile) {
            isCompleted = false
            return
        }
        visited.add(current)
        val reachableCastles = tile.exploreCastle(current.direction)
        reachableCastles.forEach { direction ->
            doExplore(PositionedDirection(direction.move(current.position), direction.opposite()))
        }
    }
}
