package io.github.hejcz.core

import kotlin.math.ceil

typealias CastleScoring = (State, Castle) -> Int

val castleScoring: CastleScoring = { state: State, castle: Castle ->
    val pointsPerEmblemAndTile = if (castle.hasCathedral(state)) 3 else 2
    calculatePoints(state, pointsPerEmblemAndTile, castle)
}

val incompleteCastleScoring: CastleScoring = { state: State, castle: Castle ->
    val pointsPerEmblemAndTile = if (castle.hasCathedral(state)) 0 else 1
    calculatePoints(state, pointsPerEmblemAndTile, castle)
}

private fun calculatePoints(state: State, pointsPerEmblemAndTile: Int, castle: Castle): Int {
    val points = pointsPerEmblemAndTile * castle.countEmblemsAndTiles()
    return when {
        state.isMageIn(castle.parts) -> points + castle.countTiles()
        state.isWitchIn(castle.parts) -> ceil(points / 2.0).toInt()
        else -> points
    }
}