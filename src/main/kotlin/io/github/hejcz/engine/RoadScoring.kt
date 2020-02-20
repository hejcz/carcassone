package io.github.hejcz.engine

import io.github.hejcz.core.Road
import io.github.hejcz.core.State
import kotlin.math.ceil

typealias RoadScoring = (State, Road) -> Int

val roadScoring: RoadScoring = { state: State, road: Road ->
    val pointsPerTile = when {
        !state.hasInnOn(road) -> 1
        else -> when {
            road.completed -> 2
            else -> 0
        }
    }
    calculatePoints(state, pointsPerTile, road)
}

private fun calculatePoints(state: State, pointsPerEmblemAndTile: Int, road: Road): Int {
    val points = pointsPerEmblemAndTile * road.tilesCount
    return when {
        state.isMageIn(road.parts) -> points + road.tilesCount
        state.isWitchIn(road.parts) -> ceil(points / 2.0).toInt()
        else -> points
    }
}