package io.github.hejcz.engine

import io.github.hejcz.api.Castle
import io.github.hejcz.api.CastleScoring
import io.github.hejcz.api.State
import kotlin.math.ceil

val castleScoring: CastleScoring = { state: State, castle: Castle ->
    val pointsPerEmblemAndTile = when {
        state.isCathedralIn(castle) -> when {
            castle.completed -> 3
            else -> 0
        }
        else -> when {
            castle.completed -> 2
            else -> 1
        }
    }
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