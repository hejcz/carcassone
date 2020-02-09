package io.github.hejcz.core

import kotlin.math.ceil

typealias CastleScoring = (State, Castle) -> Int

val castleScoring: CastleScoring = { state: State, castle: Castle ->
    val pointsPerEmblemAndTile = if (castle.hasCathedral(state)) 3 else 2
    calculatePoints(pointsPerEmblemAndTile, castle)
}

val incompleteCastleScoring: CastleScoring = { state: State, castle: Castle ->
    val pointsPerEmblemAndTile = if (castle.hasCathedral(state)) 0 else 1
    calculatePoints(pointsPerEmblemAndTile, castle)
}

private fun calculatePoints(pointsPerEmblemAndTile: Int, castle: Castle): Int {
    val points = pointsPerEmblemAndTile * castle.countEmblemsAndTiles()
    return when {
        castle.hasMage -> points + castle.countTiles()
        castle.hasWitch -> ceil(points / 2.0).toInt()
        else -> points
    }
}