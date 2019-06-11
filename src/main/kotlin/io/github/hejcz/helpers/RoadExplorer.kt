package io.github.hejcz.helpers

import io.github.hejcz.basic.tile.*
import io.github.hejcz.core.*

class RoadExplorer(private val state: State, private val initial: PositionedDirection) {
    private val visited: MutableSet<PositionedDirection> = mutableSetOf()
    private var isCompleted = true

    fun explore() = when {
        visited.isNotEmpty() -> throw IllegalStateException("Road explorer should not be reused")
        else -> doExplore(initial)
    }

    fun isCompleted() = isCompleted

    fun positions() = visited.map { it.position }.distinct()

    fun parts() = when {
        visited.isEmpty() -> throw IllegalStateException("Can't access explored road parts before calling explore method")
        else -> visited.toSet()
    }

    private fun doExplore(current: PositionedDirection) {
        if (current in visited) {
            return
        }
        when (val tile = state.tileAt(current.position)) {
            is NoTile -> {
                isCompleted = false
                return
            }
            else -> {
                visited.add(current)
                tile.exploreRoad(current.direction).forEach {
                    doExplore(PositionedDirection(it.move(current.position), it.opposite()))
                }
            }
        }
    }
}
