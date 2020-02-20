package io.github.hejcz.core

import kotlin.math.ceil

typealias RoadScoring = (State, Road) -> Int

val roadScoring: RoadScoring = { state: State, road: Road ->
    val pointsPerTile = when {
        road.hasInn(state) && road.completed -> 2
        road.hasInn(state) && !road.completed -> 0
        else -> 1
    }
    calculatePoints(state, pointsPerTile, road)
}

private fun calculatePoints(state: State, pointsPerEmblemAndTile: Int, road: Road): Int {
    val points = pointsPerEmblemAndTile * road.tilesCount
    return when {
        hasMage(state, road.parts) -> points + road.tilesCount
        hasWitch(state, road.parts) -> ceil(points / 2.0).toInt()
        else -> points
    }
}