package io.github.hejcz.engine

import io.github.hejcz.core.Castle
import io.github.hejcz.core.State
import kotlin.math.ceil

typealias CastleScoring = (State, Castle) -> Int

val castleScoring: CastleScoring = { state: State, castle: Castle ->
    val pointsPerEmblemAndTile = if (state.isCathedralIn(castle)) 3 else 2
    calculatePoints(state, pointsPerEmblemAndTile, castle)
}

val incompleteCastleScoring: CastleScoring = { state: State, castle: Castle ->
    val pointsPerEmblemAndTile = if (state.isCathedralIn(castle)) 0 else 1
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