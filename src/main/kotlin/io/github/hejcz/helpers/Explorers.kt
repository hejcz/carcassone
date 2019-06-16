package io.github.hejcz.helpers

import io.github.hejcz.basic.tile.*
import io.github.hejcz.core.*

private typealias Parts = Set<PositionedDirection>

object CastlesExplorer : DirectionsExplorer(Tile::exploreCastle)

object RoadsExplorer : DirectionsExplorer(Tile::exploreRoad)

/**
 * Applicable to roads and castles
 */
open class DirectionsExplorer(val explore: Tile.(Direction) -> Directions) {

    fun explore(state: State, position: Position, direction: Direction): Pair<Set<PositionedDirection>, Boolean> =
        explore(state, setOf(PositionedDirection(position, direction)), emptySet(), true)

    private tailrec fun explore(state: State, left: Parts, found: Parts, isCompleted: Boolean): Pair<Parts, Boolean> =
        when {
            left.isEmpty() -> found to isCompleted
            else -> {
                val current = left.first()
                val tile = state.tileAt(current.position)
                when {
                    current in found -> explore(state, left - current, found, isCompleted)
                    tile is NoTile -> explore(state, left - current, found, false)
                    else -> {
                        val otherOnTile = tile.explore(current.direction)
                        val toExplore =
                            otherOnTile.map { PositionedDirection(current.position.moveIn(it), it.opposite()) }
                        val mappedReachableFields =
                            otherOnTile.mapTo(mutableSetOf()) { PositionedDirection(current.position, it) }
                        explore(state, left - current + toExplore, found + mappedReachableFields, isCompleted)
                    }
                }
            }
        }

}

typealias GreenFieldsParts = Set<Pair<Position, Location>>

object GreenFieldsExplorer {

    fun explore(state: State, position: Position, location: Location) =
        explore(state, setOf(position to location), emptySet())

    private tailrec fun explore(state: State, left: GreenFieldsParts, found: GreenFieldsParts): GreenFieldsParts =
        when {
            left.isEmpty() -> found
            else -> {
                val current = left.first()
                val tile = state.tileAt(current.first)
                when {
                    current in found || tile is NoTile -> explore(state, left - current, found)
                    else -> {
                        val (currentPosition, currentLocation) = current
                        val reachableFields = tile.exploreGreenFields(currentLocation)
                        val toExplore = reachableFields.map { currentPosition.moveIn(it.direction) to it.mirrored() }
                        val mappedReachableFields = reachableFields.mapTo(mutableSetOf()) { currentPosition to it }
                        explore(state, left - current + toExplore, found + mappedReachableFields)
                    }
                }
            }
        }

}
