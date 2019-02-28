package io.github.hejcz.helpers

import io.github.hejcz.basic.tiles.NoTile
import io.github.hejcz.core.Location
import io.github.hejcz.core.Position
import io.github.hejcz.core.State

class GreenFieldExplorer(
    private val state: State,
    private val initialPosition: Position,
    private val initialLocation: Location
) {
    private val visited: MutableSet<LocationOnPosition> = mutableSetOf()

    @Synchronized
    fun explore() = when {
        visited.isNotEmpty() -> throw IllegalStateException("Castle explorer should not be reused")
        else -> {
            doExplore(LocationOnPosition(initialPosition, initialLocation))
        }
    }

    fun parts(): Set<Pair<io.github.hejcz.core.Position, Location>> = when {
        visited.isEmpty() -> throw IllegalStateException("Can't access explored green fields parts before calling explore method")
        else -> visited.map { Pair(it.position, it.location) }.toSet()
    }

    private fun doExplore(current: LocationOnPosition) {
        if (visited.contains(current)) {
            return
        }
        when (val tile = state.tileAt(current.position)) {
            is NoTile -> {
                return
            }
            else -> {
                val explored = tile.exploreGreenFields(current.location)
                visited.addAll(explored.map { LocationOnPosition(current.position, it) })
                explored.forEach { location ->
                    doExplore(LocationOnPosition(location.direction.move(current.position), location.opposite()))
                }
            }
        }
    }

    companion object {

        data class LocationOnPosition(val position: Position, val location: Location)

    }
}
