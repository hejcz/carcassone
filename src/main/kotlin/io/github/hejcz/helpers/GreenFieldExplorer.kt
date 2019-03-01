package io.github.hejcz.helpers

import io.github.hejcz.basic.tiles.*
import io.github.hejcz.core.*

private data class LocationOnPosition(val position: Position, val location: Location)

class GreenFieldExplorer(
    private val state: State,
    private val initialPosition: Position,
    private val initialLocation: Location
) {
    fun explore(): Set<Pair<Position, Location>> {
        val cache = mutableSetOf<LocationOnPosition>()
        doExplore(LocationOnPosition(initialPosition, initialLocation), cache)
        return cache.map { Pair(it.position, it.location) }.toSet()
    }

    private fun doExplore(
        current: LocationOnPosition,
        cache: MutableSet<LocationOnPosition>
    ) {
        if (current in cache) {
            return
        }
        val tile = state.tileAt(current.position)
        if (tile !is NoTile) {
            val explored = tile.exploreGreenFields(current.location)
            cache.addAll(explored.map { LocationOnPosition(current.position, it) })
            explored.forEach { location ->
                doExplore(LocationOnPosition(location.direction.move(current.position), location.mirrored()), cache)
            }
        }
    }

}
