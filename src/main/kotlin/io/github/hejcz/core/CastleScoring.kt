package io.github.hejcz.core

typealias CastleScoring = (State, Castle) -> Int

val scoreCompletedCastle = { _: State, castle: Castle -> 2 * castle.countEmblemsAndTiles() }

val scoreIncompleteCastle = { _: State, castle: Castle -> castle.countEmblemsAndTiles() }

val scoreCompletedCastleWithCathedral = { state: State, castle: Castle ->
    castle.countEmblemsAndTiles() * if (castle.hasCathedral(state)) 3 else 2
}

val scoreIncompleteCastleWithCathedral = { state: State, castle: Castle ->
    if (castle.hasCathedral(state)) 0 else castle.countEmblemsAndTiles()
}