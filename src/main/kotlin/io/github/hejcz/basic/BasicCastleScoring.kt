package io.github.hejcz.basic

import io.github.hejcz.core.State
import io.github.hejcz.helpers.ExploredCastle

object BasicCastleScoring : CastleScoringStrategy {
    override fun score(state: State, exploredCastle: ExploredCastle): Int = 2 * (exploredCastle.emblems + exploredCastle.tilesCount)
}
