package io.github.hejcz.basic

import io.github.hejcz.core.State
import io.github.hejcz.helpers.ExploredCastle

object BasicIncompleteCastleScoring : IncompleteCastleScoringStrategy {
    override fun score(state: State, exploredCastle: ExploredCastle): Int = exploredCastle.emblems + exploredCastle.tilesCount
}
