package io.github.hejcz.basic

import io.github.hejcz.core.State
import io.github.hejcz.helpers.ExploredCastle

interface CastleScoringStrategy {

    fun score(state: State, exploredCastle: ExploredCastle): Int

}
