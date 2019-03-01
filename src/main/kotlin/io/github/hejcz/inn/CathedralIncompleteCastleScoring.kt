package io.github.hejcz.inn

import io.github.hejcz.basic.IncompleteCastleScoringStrategy
import io.github.hejcz.core.State
import io.github.hejcz.helpers.ExploredCastle
import io.github.hejcz.inn.tiles.InnTile

object CathedralIncompleteCastleScoring : IncompleteCastleScoringStrategy {
    override fun score(state: State, exploredCastle: ExploredCastle): Int {
        val multiplier = if (exploredCastle.hasAnyCathedral(state)) 0 else 1
        return (exploredCastle.emblems + exploredCastle.tilesCount) * multiplier
    }
}

fun ExploredCastle.hasAnyCathedral(state: State) =
    this.pieces.asSequence()
        .map { state.tileAt(it.position) }
        .any { it is InnTile && it.hasCathedral() }
