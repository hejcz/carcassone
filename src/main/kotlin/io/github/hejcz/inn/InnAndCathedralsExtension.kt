package io.github.hejcz.inn

import io.github.hejcz.basic.rule.*
import io.github.hejcz.core.*
import io.github.hejcz.setup.*

object InnAndCathedralsExtension : Extension {
    override fun modify(rulesSetup: RulesSetup) {
        rulesSetup.replace(RewardCompletedCastle::class, RewardCompletedCastle(CompletedCastleWithCathedralScoring))
        rulesSetup.replace(RewardIncompleteCastle::class, RewardIncompleteCastle(IncompleteCastleWithCathedralScoring))
        rulesSetup.replace(RewardCompletedRoad::class, RewardCompletedRoad(InnRoadScoring))
        rulesSetup.replace(RewardIncompleteRoad::class, RewardIncompleteRoad(InnRoadScoring))
    }

    override fun modify(piecesSetup: PiecesSetup) = piecesSetup.add(BigPiece)
}
