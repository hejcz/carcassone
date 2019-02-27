package io.github.hejcz.rules.helpers

import io.github.hejcz.engine.State
import io.github.hejcz.placement.PositionedDirection
import io.github.hejcz.tiles.basic.NoTile

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