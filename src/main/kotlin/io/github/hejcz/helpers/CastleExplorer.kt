package io.github.hejcz.helpers

import io.github.hejcz.basic.tiles.NoTile
import io.github.hejcz.core.PositionedDirection
import io.github.hejcz.core.State

class CastleExplorer(private val state: State, private val initial: PositionedDirection) {
    private val visited: MutableSet<io.github.hejcz.core.PositionedDirection> = mutableSetOf()
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
        if (visited.contains(current)) {
            return
        }
        when (val tile = state.tileAt(current.position)) {
            is NoTile -> {
                isCompleted = false
                return
            }
            else -> {
                visited.add(current)
                tile.exploreCastle(current.direction).forEach {
                    doExplore(PositionedDirection(it.move(current.position), it.opposite()))
                }
            }
        }
    }
}
