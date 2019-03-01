package io.github.hejcz.inn

import io.github.hejcz.basic.CastleScoringStrategy
import io.github.hejcz.core.State
import io.github.hejcz.helpers.ExploredCastle
import io.github.hejcz.inn.tiles.InnTile

object CathedralCastleScoring : CastleScoringStrategy {
    override fun score(state: State, exploredCastle: ExploredCastle): Int {
       val multiplier = if (exploredCastle.hasAnyCathedral(state)) 3 else 2
        return (exploredCastle.emblems + exploredCastle.tilesCount) * multiplier
    }
}
