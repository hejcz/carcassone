package io.github.hejcz.core

import kotlin.math.ceil

typealias RoadScoring = (State, Road) -> Int

val roadScoring: RoadScoring = { state: State, road: Road ->
    val pointsPerTile = when {
        road.hasInn(state) && road.completed -> 2
        road.hasInn(state) && !road.completed -> 0
        else -> 1
    }
    calculatePoints(pointsPerTile, road)
}

private fun calculatePoints(pointsPerEmblemAndTile: Int, road: Road): Int {
    val points = pointsPerEmblemAndTile * road.tilesCount
    return when {
        road.hasMage -> points + road.tilesCount
        road.hasWitch -> ceil(points / 2.0).toInt()
        else -> points
    }
}