package io.github.hejcz.helpers

import io.github.hejcz.basic.tile.*
import io.github.hejcz.core.*

typealias GreenFields = Set<Pair<Position, Location>>

object TailRecGreenFieldExplorer {

    fun explore(state: State, position: Position, location: Location) =
        explore(state, setOf(position to location), emptySet())

    private tailrec fun explore(state: State, left: GreenFields, found: GreenFields): GreenFields = when {
        left.isEmpty() -> found
        else -> {
            val toCheck = left.first()
            val tile = state.tileAt(toCheck.first)
            when {
                toCheck in found || tile is NoTile -> explore(state, left - toCheck, found)
                else -> {
                    val reachableFields = tile.exploreGreenFields(toCheck.second)
                    val toExplore = reachableFields
                        .map { it.direction.move(toCheck.first) to it.mirrored() }
                    val mappedReachableFields = reachableFields.mapTo(mutableSetOf()) { toCheck.first to it }
                    explore(state, left - toCheck + toExplore, found + mappedReachableFields)
                }
            }
        }
    }

}
